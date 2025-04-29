from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters.character import CharacterTextSplitter
from langchain_openai import OpenAIEmbeddings
from langchain_community.vectorstores import FAISS
from langchain.chains import create_retrieval_chain
from langchain.chains.combine_documents import create_stuff_documents_chain
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI
from langchain_core.rate_limiters import InMemoryRateLimiter
from langchain_text_splitters import MarkdownHeaderTextSplitter
import os
from dotenv import load_dotenv
import requests
from io import BytesIO

# Load environment variables
load_dotenv()

if not os.getenv("OPENAI_API_KEY"):
    raise ValueError("Please set your OpenAI API key in the .env file.")

class PDFQuerySystem:
    def __init__(self):
        self.embeddings = OpenAIEmbeddings(
            model="text-embedding-3-large"  
            # With the `text-embedding-3` class  
            # of models, you can specify the size  
            # of the embeddings you want returned.  
            # dimensions=1024
        )
        self.vector_store = None
        self.qa_chain = None

    def load_pdfs(self, pdf_folder_path):
        if not os.path.isdir(pdf_folder_path):
            raise ValueError(f"The path {pdf_folder_path} is not a directory")
        
        
        documents = []
        pdf_count = 0
    
        # Load all PDFs from the folder
        for file in os.listdir(pdf_folder_path):
            if file.endswith('.pdf'):
                pdf_count += 1
                pdf_path = os.path.join(pdf_folder_path, file)
                try:
                    loader = PyPDFLoader(pdf_path)
                    documents.extend(loader.load())
                    print(f"Successfully loaded: {file}")
                except Exception as e:
                    print(f"Error loading {file}: {str(e)}")
    
        if pdf_count == 0:
            raise ValueError(f"No PDF files found in {pdf_folder_path}")

        # Split documents into chunks of text
        text_splitter = CharacterTextSplitter(
            chunk_size=1000, # Size of each chunk
            chunk_overlap=200, # Overlap between chunks
            separator="\n" # Separator for splitting text
        )

        doc_chunks = text_splitter.split_documents(documents)

        # Create vector store
        # Facebook AI Similarity Search (FAISS) is a library for efficient similarity search and clustering of dense vectors.
        self.vector_store = FAISS.from_documents(doc_chunks, self.embeddings)
        """ 
        Written by github copilot but when I looked up RetrievalQA on LangChain's documentation, 
        I found that it is deprecated.
            # Initialize QA chain
            self.qa_chain = RetrievalQA.from_chain_type(
                llm = OpenAI(temperature=0), 
                chain_type = "stuff",
                retriever = self.vector_store.as_retriever()
            )
        """

        # wrapper around the vector store class 
        # to make it conform to the retriever interface. It uses the search methods implemented 
        # by a vector store, like similarity search and MMR, to query the texts in the vector store.
        retriever = self.vector_store.as_retriever()
        
        rate_limiter = InMemoryRateLimiter(
            requests_per_second=1,  # <-- Super slow! We can only make a request once every 1 seconds!!
            check_every_n_seconds=0.1,  # Wake up every 100 ms to check whether allowed to make a request,
            max_bucket_size=10,  # Controls the maximum burst size.
        )

        # temp = 0 for safe results temp > 0 for creative yet risky results
        llm = ChatOpenAI(model= "gpt-4o-mini", temperature=.5, rate_limiter=rate_limiter)

        #System prompt needed {context} to use the vector store and reteriver data and similarity search
        system_prompt = """
        You are a helpful assistant that answers questions based on the provided documents.
        Those documents contain information about the student including but not limited to
        the course status sheet listing the required courses for graduation and the users 
        current college transcript listing the courses that the student has already taken and 
        each courses repective grade.
        You should determine the user's majors from the documents given.
        The user will ask you a question about their college courses and you will answer based on the context provided.
        
        Context from documents: {context}
        Question: {input}
        Please provide a detailed answer based on the context provided.
        """


        prompt = ChatPromptTemplate.from_messages(
            [
                ("system", system_prompt),
                ("human", "{input}"),
            ]
        )

        
        # "stuff" : Combines all relevant documents into one prompt
        question_ans_chain = create_stuff_documents_chain(llm,prompt=prompt)

        # Create the retrieval chain that will:
        # 1. Take the question
        # 2. Use the retriever to get relevant documents
        # 3. Pass those documents as context to the QA chain
        self.qa_chain = create_retrieval_chain(retriever, question_ans_chain)

    def load_pdfs_from_urls(self, pdf_urls):
        if not isinstance(pdf_urls, list) or not all(isinstance(url, str) for url in pdf_urls):
            raise ValueError("The input must be a list of URLs (strings).")

        documents = []
        pdf_count = 0

        for url in pdf_urls:
            if url.endswith('.pdf'):
                pdf_count += 1
                try:
                    loader = PyPDFLoader(url)
                    documents.extend(loader.load())
                    print(f"Successfully loaded: {url}")
                except Exception as e:
                    print(f"Error loading {url}: {str(e)}")

        if pdf_count == 0:
            raise ValueError("No valid PDFs were loaded from the provided URLs.")

        # Split documents into chunks of text
        text_splitter = CharacterTextSplitter(
            chunk_size=1000,  # Size of each chunk
            chunk_overlap=200,  # Overlap between chunks
            separator="\n"  # Separator for splitting text
        )

        doc_chunks = text_splitter.split_documents(documents)

        # Create vector store
        self.vector_store = FAISS.from_documents(doc_chunks, self.embeddings)

        # Initialize retriever and QA chain as before
        retriever = self.vector_store.as_retriever()
        rate_limiter = InMemoryRateLimiter(
            requests_per_second=1,
            check_every_n_seconds=0.1,
            max_bucket_size=10,
        )
        llm = ChatOpenAI(model="gpt-4o-mini", temperature=0.5, rate_limiter=rate_limiter)
        system_prompt = """
        You are a helpful assistant that answers questions based on the provided documents.
        Those documents contain information about the student including but not limited to
        the course status sheet listing the required courses for graduation and the users
        current college transcript listing the courses that the student has already taken and
        each courses respective grade.
        The User's majors should be determined by the documents provided.
        The user will ask you a question about their college courses and you will answer based on the context provided.

        Context from documents: {context}
        Question: {input}
        Please provide a detailed answer based on the context provided.
        """
        prompt = ChatPromptTemplate.from_messages(
            [
                ("system", system_prompt),
                ("human", "{input}"),
            ]
        )
        question_ans_chain = create_stuff_documents_chain(llm, prompt=prompt)
        self.qa_chain = create_retrieval_chain(retriever, question_ans_chain)

    #had to update code to use invoke() instead of run() becasue run() is deprecated
    def query(self, question):
        if not self.qa_chain:
            raise Exception("Please load PDFs first using load_pdfs()")
        
        # invoke() now only needs the input question
        # The retrieval chain automatically:
        # 1. Takes the question
        # 2. Uses the retriever to find relevant document chunks from FAISS
        # 3. Combines those chunks into the context
        # 4. Passes both question and context to the LLM
        response = self.qa_chain.invoke({
            "input": question,
            })
        return response["answer"]

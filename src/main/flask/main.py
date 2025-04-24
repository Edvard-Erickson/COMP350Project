import PyAIQuery as pq
from flask import Flask, jsonify, request , render_template
from flask_restful import Resource, Api

# creating the flask app 
app = Flask(__name__) 
# creating an API object 
api = Api(app) 

# Initialize PDF system at startup
pdf_system = None
def initialize_pdf_system():
    global pdf_system
    if pdf_system is None:
        print("Initializing PDF Query System...")
        pdf_system = pq.PDFQuerySystem()
        pdf_folder_path = "C:/Users/MITCHELLAL21/OneDrive - Grove City College/1. Classes/3. Junior 24.25/Spring 2025/COMP 350 SWE/Sprint2/pdfs"
        pdf_system.load_pdfs(pdf_folder_path)
        print("PDF Query System initialized successfully!")
    else:
        print("PDF Query System already initialized.")

@app.route('/')
def home():
    return render_template('index.html')

class Response(Resource):    
    def post(self):
        global pdf_system
        data = request.get_json()

        # Ask questions
        question = data.get("question")
        """TODO make sure this is a valid way to read json major
        userMajor = data.get("major")
        if Not userMajor:
            userMajor = "Unspecified Major"
        """        
        if not question:
            return jsonify({"error": "No question provided"}), 400
        answer = pdf_system.query(question, major="Computer Engineering") # hard coded major for now
        print(f"Question: {question}")
        
        return jsonify({"answer": answer})

api.add_resource(Response, '/ask')

if __name__ == "__main__":
    initialize_pdf_system() # Initialize PDF system
    app.run(debug=True,port=8000) # Run the Flask app
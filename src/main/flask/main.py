import PyAIQuery as pq
from flask import Flask, jsonify, request , render_template, g
from flask_restful import Resource, Api
from flask_cors import CORS

# creating the flask app 
app = Flask(__name__) 
# creating an API object 
api = Api(app)
CORS(app)

# Initialize PDF system at startup
pdf_system = None
def initialize_pdf_system():
    global pdf_system
    if pdf_system is None:
        print("Initializing PDF Query System...")
        pdf_system = pq.PDFQuerySystem()
        print("PDF Query System initialized successfully!")
    else:
        print("PDF Query System already initialized.")

def initialize_pdf_system_from_urls(urls):
    global pdf_system
    print("Initializing PDF Query System...")
    pdf_system = pq.PDFQuerySystem()
    pdf_system.load_pdfs_from_urls(urls)
    print("PDF Query System initialized successfully!")

@app.route('/api/index')
def home():
    return render_template('index.html')

@app.route('/api/setMajors')
def setMajors():
     majors = request.args.get('majors')
     names = request.args.get('names')
     urls = []
     if majors != "":
         urls = majors.split(",")
     else:
         urls = ["https://www.gcc.edu/Portals/0/2024-25-Catalog.pdf"]
     print(urls)
     initialize_pdf_system_from_urls(urls)
     print("initialized")
     return jsonify({"message": "Majors set successfully!"}), 200

class Response(Resource):
    def post(self):
        global pdf_system
        data = request.get_json()

        # Ask questions
        question = data.get("question")
        if not question:
            return jsonify({"error": "No question provided"}), 400
        answer = pdf_system.query(question)
        print(f"Question: {question}")

        return jsonify({"answer": answer})

api.add_resource(Response, '/api/ask')

if __name__ == "__main__":
    initialize_pdf_system() # Initialize PDF system
    app.run(debug=True,port=8000) # Run the Flask app
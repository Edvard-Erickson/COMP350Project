// general app that loads all of the components as necessary. Always features a navigation bar.
import './App.css'
import HomePage from './components/HomePage.js'
import Page404 from './components/Page404.js'
import SettingsPage from './components/SettingsPage.js'
import { AddCourses } from './components/AddCourses.js'
import ViewCourses from './components/ViewCourses.js'
import Schedule from './components/Schedule.js'
import Generate from './components/Generate.js'
import Login from './components/Login.js'
import Register from './components/Register.js';


import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Container from "react-bootstrap/Container"
import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'
import NavDropdown from 'react-bootstrap/NavDropdown'
import { getAuth, onAuthStateChanged, signOut } from 'firebase/auth'
import { useState, useEffect } from 'react'
import Button from 'react-bootstrap/Button'
import Collapse from 'react-bootstrap/Collapse'
import 'bootstrap/dist/css/bootstrap.min.css'
import cookies from 'react-cookies'
import { Link } from "react-router-dom";

export default function App() {

  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [collapse, setCollapse] = useState(true);
  const [question, setQuestion] = useState('');
  const [response, setResponse] = useState('');
  const [coursesRemembered, setCoursesRemembered] = useState('');

  useEffect(() => {
    const auth = getAuth();
    const unsubscribe = onAuthStateChanged(auth, (u) => {
      setUser(u);
      setLoading(false);
    });
    return () => unsubscribe();
  }, []);

  if (loading) {
    return <p>Loading...</p>; // optional, to avoid flash
  }



    const handleSubmit = async (e) => {
        e.preventDefault();
        setResponse('loading...');

        if (coursesRemembered != cookies.load('selectedCourses')) {
            var urls = cookies.load('programs').map(program => program.href).join(',');
            var names = cookies.load('programs').map(program => program.name).join(',');
            await fetch(`http://localhost:8000/api/setMajors?majors=${urls}&names=${names}`)
             .then(response => {
                 if (!response.ok) {
                     throw new Error('Network response was not ok');
                 }
             })
             .catch(error => {
                 console.log("error: ", error);
              });
            setCoursesRemembered(cookies.load('selectedCourses'));
        }

        await fetch('http://localhost:8000/api/ask', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                question: question
            })
        })
        .then(response => response.json())
        .then(data => {
            setResponse(data.answer);
        })
        .catch(error => {
            setResponse('Error: ' + error.message);
        });

        setQuestion('');
    }

  return (
      <Router>
        {/* Show Navbar and chatbox only if logged in */}
        {user && (
          <>
            <Navbar expand="lg" className="bg-body-tertiary">
              <Container>
                <Nav>
                  <Nav.Link as={Link} to="/">Home</Nav.Link>
                  <Nav.Link as={Link} to="/settings">Settings</Nav.Link>
                  <Button variant="outline-danger" onClick={() => signOut(getAuth())}>
                    Logout
                  </Button>
                </Nav>
              </Container>
            </Navbar>

            <div id="chatbox" className={collapse ? "collapsed" : ""}>
              <div id="chatbox-expand" onClick={() => setCollapse(!collapse)}>
                <h2>▴ AIden - Powered by OpenAI</h2>
              </div>
              <div id="chatbox-content">
                <div id="chatbox-messages">
                  <p>{response}</p>
                </div>
                <form id="chatbox-input" onSubmit={handleSubmit}>
                  <input
                    type="text"
                    placeholder="Ask a question"
                    maxLength="150"
                    value={question}
                    onChange={(e) => setQuestion(e.target.value)}
                  />
                  <button type="submit" className="button">Send</button>
                </form>
              </div>
            </div>
          </>
        )}

        <Routes>
          {!user ? (
            <>
              <Route path="/" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="*" element={<Login />} />
            </>
          ) : (
            <>
              <Route path="/" element={<HomePage />} />
              <Route path="/settings" element={<SettingsPage />} />
              <Route path="/schedule" element={<Schedule />} />
              <Route path="/generate" element={<Generate />} />
              <Route path="/viewCourses" element={<ViewCourses />} />
              <Route path="/addCourses" element={<AddCourses />} />
              <Route path="*" element={<Page404 />} />
            </>
          )}
        </Routes>
      </Router>

  );
};

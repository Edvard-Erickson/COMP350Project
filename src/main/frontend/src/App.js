// general app that loads all of the components as necessary. Always features a navigation bar.
import './App.css'
import HomePage from './components/HomePage.js'
import Page404 from './components/Page404.js'
import SettingsPage from './components/SettingsPage.js'
import { AddCourses } from './components/AddCourses.js'
import ViewCourses from './components/ViewCourses.js'
import Schedule from './components/Schedule.js'
import Generate from './components/Generate.js'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Container from "react-bootstrap/Container"
import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'
import { useState, useEffect } from 'react'
import Button from 'react-bootstrap/Button'
import Collapse from 'react-bootstrap/Collapse'
import 'bootstrap/dist/css/bootstrap.min.css'

export default function App() {
    const [collapse, setCollapse] = useState(true);
    const [question, setQuestion] = useState('');
    const [response, setResponse] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setResponse('loading...');

        fetch('http://localhost:8000/api/ask', {
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
    };

  return (
    <Router>
        <Navbar expand="lg" className="bg-body-tertiary">
            <Container>
                <Nav>
                    <Nav.Link href="/">Home</Nav.Link>
                    <Nav.Link href="/settings">Settings</Nav.Link>
                </Nav>
            </Container>
        </Navbar>
        <div id="chatbox" className={collapse ? "collapsed" : ""}>
            <div id="chatbox-expand" onClick={() => setCollapse(!collapse)}>
                <h2>▴ AI Assistant - Powered by OpenAI</h2>
            </div>
            <div id="chatbox-content">
                <div id="chatbox-messages">
                    <p>{response}</p>
                </div>
                <form id="chatbox-input" onSubmit={handleSubmit}>
                    <input type="text" placeholder="Ask a question" maxlength="150" value={question} onChange={(e) => setQuestion(e.target.value)}/>
                    <button type="submit" className="button">Send</button>
                </form>
            </div>
        </div>
        <Routes>
            <Route exact path="/" element=<HomePage /> />
            <Route path="/settings" element=<SettingsPage /> />
            <Route path="/schedule" element=<Schedule /> />
            <Route path="/generate" element=<Generate /> />
            <Route path="/viewCourses" element=<ViewCourses /> />
            <Route path="/addCourses" element=<AddCourses /> />
            <Route path="/*" element=<Page404 /> />
        </Routes>
    </Router>
  );
};

import './App.css'
import HomePage from './components/HomePage.js'
import Page404 from './components/Page404.js'
import SettingsPage from './components/SettingsPage.js'
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom"
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container"
import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'
import NavDropdown from 'react-bootstrap/NavDropdown'
import 'bootstrap/dist/css/bootstrap.min.css';

export default function App() {
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
        <Routes>
            <Route exact path="/" element=<HomePage /> />
            <Route path="/settings" element=<SettingsPage /> />
            <Route path="/schedule" element=<Page404 /> />
            <Route path="/generate" element=<Page404 /> />
            <Route path="/viewGuides" element=<Page404 /> />
            <Route path="/viewCourses" element=<Page404 /> />
            <Route path="/addCourses" element=<Page404 /> />
            <Route path="/*" element=<Page404 /> />
        </Routes>
    </Router>
  );
};

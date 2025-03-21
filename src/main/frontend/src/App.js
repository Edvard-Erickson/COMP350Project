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
            <Route path="/schedule" element=<Schedule /> />
            <Route path="/generate" element=<Generate /> />
            <Route path="/viewCourses" element=<ViewCourses /> />
            <Route path="/addCourses" element=<AddCourses /> />
            <Route path="/*" element=<Page404 /> />
        </Routes>
    </Router>
  );
};

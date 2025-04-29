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


import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom"
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container"
import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'
import NavDropdown from 'react-bootstrap/NavDropdown'
import 'bootstrap/dist/css/bootstrap.min.css';
import { getAuth, onAuthStateChanged, signOut } from 'firebase/auth'
import { useState, useEffect } from 'react'

export default function App() {

  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

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

  return (
      <Router>
        {/* Show Navbar only if logged in */}
        {user && (
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
              <Route exact path="/" element={<HomePage />} />
              <Route path="/settings" element={<SettingsPage />} />
              <Route path="/schedule" element={<Schedule />} />
              <Route path="/generate" element={<Generate />} />
              <Route path="/viewCourses" element={<ViewCourses />} />
              <Route path="/addCourses" element={<AddCourses />} />
              <Route path="/*" element={<Page404 />} />
            </>
          )}
        </Routes>

      </Router>
    );
  }

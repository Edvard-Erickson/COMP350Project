// this component handles the home page (localhost:3000/).
import { useEffect, useState } from 'react'
import { Link } from "react-router-dom"
import Image from 'react-bootstrap/Image'
import Button from 'react-bootstrap/Button'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Dropdown from 'react-bootstrap/Dropdown'
import DropdownButton from 'react-bootstrap/DropdownButton'

const HomePage = () => {
    return (
        <div className="mainContainer">
            <h2 id='homePageGreeting'>GCC Schedule Helper</h2>
            <Container id='homeButtons'>
                <Col>
                    <Row><Link to='/schedule' className="button main">View Schedule</Link></Row>
                    <Row>
                        <Dropdown className='custom-dropdown' drop='end'>
                            <Dropdown.Toggle variant="success" id="dropdown-basic" className="button">
                                Course Guides
                            </Dropdown.Toggle>

                            <Dropdown.Menu>
                                {document.cookie.split('; ').map(cookie => {
                                    const [name, value] = cookie.split('=');
                                    if (name === 'programs') {
                                        return JSON.parse(decodeURIComponent(value)).map((program, index) => (
                                            <Dropdown.Item key={index} href={program.href} className="custom-dropdown-item button">
                                                {program.name}
                                            </Dropdown.Item>
                                        ));
                                    }
                                    return null;
                                })}
                            </Dropdown.Menu>
                        </Dropdown>
                    </Row>
                    <Row><Link to='/viewCourses' className="button main">View Courses</Link></Row>
                    <Row><Link to='/addCourses' className="button main">Add Courses</Link></Row>
                </Col>
            </Container>
        </div>
    );
};

export default HomePage;
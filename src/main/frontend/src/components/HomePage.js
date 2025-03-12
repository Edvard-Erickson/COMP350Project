import { useEffect, useState } from 'react'
import { Link } from "react-router-dom"
import Image from 'react-bootstrap/Image'
import Button from 'react-bootstrap/Button'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

const HomePage = () => {
    return (
        <div>
        <h2 id='homePageGreeting'>Hello User!</h2>
            <Container id='homeButtons'>
                <Col>
                    <Row><Link to='/generate'>Generate Schedules</Link></Row>
                    <Row><Link to='/viewGuides'>View Course Guides</Link></Row>
                    <Row><Link to='/viewCourses'>View Courses</Link></Row>
                    <Row><Link to='/addCourses'>Add Courses</Link></Row>
                </Col>
            </Container>
        </div>
    )
}

export default HomePage;
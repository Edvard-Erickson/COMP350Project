/* handles the view courses page, where the user can see courses they have on their schedule currently. They can also
 remove these courses from their schedule */
import { useEffect, useState } from 'react'
import { Link } from "react-router-dom"
import cookies from 'react-cookies'
import Button from 'react-bootstrap/Button'
import { groupTimes, sortDays } from './AddCourses.js'

const ViewCourses = () => {
    const [courses, setCourses] = useState([]);
    const [timeBlocks, setTimeBlocks] = useState([]);

    const fetchCourses = () => {
        const selectedCourses = cookies.load('selectedCourses') || [];
        if (selectedCourses.length > 0) {
            fetch(`http://localhost:8080/api/coursesInfo?courses=${selectedCourses.join(',')}`)
                .then(response => response.json())
                .then(data => {
                    setCourses(data);
                    console.log("courses: ", data);
                })
                .catch(error => console.error('Error fetching data:', error));
        } else {
            setCourses([]);
        }
    };

    const fetchTimeBlocks = () => {
        const savedTimeBlocks = cookies.load('timeBlocks') || [];
        setTimeBlocks(savedTimeBlocks);
    };

    useEffect(() => {
        fetchCourses();
    }, []);

    const removeCourses = () => {
        const selectedCourses = Array.from(document.querySelectorAll('.check:checked')).map((checkbox) => checkbox.id);
        var existingCourses = cookies.load('selectedCourses') || [];
        existingCourses = existingCourses.filter(course => !selectedCourses.includes(course));

        console.log(existingCourses);
        if (selectedCourses.length === 0) {
            alert('No courses selected or already added');
            return;
        }
        cookies.save('generateCourseList', existingCourses);
        cookies.save('selectedCourses', existingCourses);
        alert('Selected courses removed');
        fetchCourses();
    }

    const toggleRowHighlight = () => {
        let ids = [];
        for (let element of document.querySelectorAll('.check:checked')) {
            ids.push(element.id);
        }
        for (let element of document.querySelectorAll('tr')) {
            console.log(ids);
            console.log(element.id);
            if (ids.includes(element.id)) {
                element.className = 'highlighted-row pointer';
            } else {
                if (element.className === 'highlighted-row pointer' || element.className === 'pointer') {
                    element.className = 'pointer';
                } else {
                    element.className = '';
                }
            }
        }
    };

    return (
        <div>
            <div id="headingView">
                <h2>Current Courses</h2>
                <Link to="/addCourses" className="viewCourseButton linkButton button">Go to Add Courses</Link>
            </div>
            <table className="fullWidthTable">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Course</th>
                                        <th>Name</th>
                                        <th>Section</th>
                                        <th>Professor</th>
                                        <th>Times</th>
                                        <th>Semester</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {courses.map((course) => (
                                        <tr
                                            key={`${course.department}${course.courseCode}${course.section}${course.semester}`}
                                            id={`${course.department}${course.courseCode}${course.section}${course.semester}`}
                                            className="pointer"
                                            onClick={(e) => {
                                                if (!e.target.classList.contains('check')) {
                                                    const checkbox = e.currentTarget.querySelector(`#${course.department}${course.courseCode}${course.section}${course.semester}`);
                                                    checkbox.click();
                                                }
                                                toggleRowHighlight();
                                            }}
                                        >
                                            <td><input type="checkbox" className='check' id={`${course.department}${course.courseCode}${course.section}${course.semester}`} onClick={ toggleRowHighlight }></input></td>
                                            <td>{course.department}{course.courseCode}</td>
                                            <td>{course.name}</td>
                                            <td>{course.section}</td>
                                            <td>{course.professor}</td>
                                            <td>
                                                {Object.entries(groupTimes(course.times)).map(([time, days]) => (
                                                    <span key={time}> {sortDays(days).join('')}: {time} </span>
                                                ))}
                                            </td>
                                            <td>{course.semester}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
            <Button as={Link} onClick={removeCourses} className="viewCourseButton buttonButton button">Remove Selected Courses</Button>
        </div>
    )
}

export default ViewCourses;
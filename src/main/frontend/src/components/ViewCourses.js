import { useEffect, useState } from 'react'
import { Link } from "react-router-dom"
import cookies from 'react-cookies'
import Button from 'react-bootstrap/Button'
import { groupTimes, sortDays } from './AddCourses.js'

const ViewCourses = () => {
    const [courses, setCourses] = useState([]);

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
        cookies.save('selectedCourses', existingCourses, { path: '/' });
        alert('Selected courses removed');
        fetchCourses();
    }

    return (
        <div>
            <h2>Current Courses</h2>
            <table className="fullWidthTable">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Course</th>
                                        <th>Name</th>
                                        <th>Section</th>
                                        <th>Professor</th>
                                        <th>Times</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {courses.map((course) => (
                                        <tr key={`${course.department}${course.courseCode}${course.section}`}>
                                            <td><input type="checkbox" className='check' id={`${course.department}${course.courseCode}${course.section}`}></input></td>
                                            <td>{course.department}{course.courseCode}</td>
                                            <td>{course.name}</td>
                                            <td>{course.section}</td>
                                            <td>{course.professor}</td>
                                            <td>
                                                {Object.entries(groupTimes(course.times)).map(([time, days]) => (
                                                    <span key={time}> {sortDays(days).join('')}: {time} </span>
                                                ))}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
            <Button as={Link} onClick={removeCourses} className="viewCourseButton buttonButton">Remove Selected Courses</Button>
            <Link to="/addCourses" className="viewCourseButton linkButton">Go to Add Courses</Link>
        </div>
    )
}

export default ViewCourses;
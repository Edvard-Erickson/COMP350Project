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
            <ul className='scrollList'>
                {courses.map((course) => (
                    <li key={`${course.department}${course.courseCode}${course.section}`}>
                    <input type="checkbox" className='check' id={`${course.department}${course.courseCode}${course.section}`}></input>
                    <span className='courseInfo'>
                        {course.department}{course.courseCode} | {course.name} | {course.section} |
                        {Object.entries(groupTimes(course.times)).map(([time, days]) => (
                            <span key={time}> {sortDays(days).join('')}: {time} </span>
                        ))}
                    </span>
                    </li>
                ))}
            </ul>
            <Button onClick={removeCourses}>Remove Selected Courses</Button>
        </div>
    )
}

export default ViewCourses;
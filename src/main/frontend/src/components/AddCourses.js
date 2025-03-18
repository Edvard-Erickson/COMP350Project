import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import { Form, FormControl, Button } from 'react-bootstrap';
import cookies from 'react-cookies';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';

export const groupTimes = (times) => {
        const grouped = {};
        for (const [day, time] of Object.entries(times)) {
            const timeKey = `${time[0]} - ${time[1]}`;
            if (!grouped[timeKey]) {
                grouped[timeKey] = [];
            }
            grouped[timeKey].push(day);
        }
        return grouped;
};

export const sortDays = (days) => {
    const dayOrder = ['M', 'T', 'W', 'R', 'F', 'S', 'U'];
    return days.sort((a, b) => dayOrder.indexOf(a) - dayOrder.indexOf(b));
};

export const AddCourses = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [results, setResults] = useState([]);
    const [filteredResults, setFilteredResults] = useState([]);
    const [showFilterForm, setShowFilterForm] = useState(false);
    const [selectedDepartment, setSelectedDepartment] = useState('');
    const [professor, setProfessor] = useState('');
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');

    useEffect(() => {
        updateResults();
    }, [selectedDepartment, professor, startTime, endTime, searchQuery]);

    const updateResults = () => {
        var sTime = '00:00:00';
        var eTime = '23:59:59';
        if (convertToMilitaryTime(startTime) != null) {
            sTime = convertToMilitaryTime(startTime);
            console.log(sTime);
        }
        if (convertToMilitaryTime(endTime) != null) {
            eTime = convertToMilitaryTime(endTime);
            console.log(eTime);
        }
        const query = searchQuery ? `/${searchQuery}` : '';
        const url = `http://localhost:8080/api/search${query}?department=${selectedDepartment}&professor=${professor}&times=${sTime}-${eTime}`;
        console.log(`Fetching URL: ${url}`);
        setResults(
            fetch(url)
            .then(response => response.json())
            .then(data => {
                setResults(data);
                setFilteredResults(data);
                console.log(data);
            })
            .catch(error => console.error('Error fetching data:', error))
        );
    }

    const convertToMilitaryTime = (time) => {
        const timePattern = /^(1[0-2]|0?[1-9]):([0-5][0-9])\s?(AM|PM)$/i;
        const match = time.match(timePattern);
        if (!match) return null;

        let [ , hours, minutes, period ] = match;
        hours = parseInt(hours, 10);
        if (period.toUpperCase() === 'PM' && hours !== 12) {
            hours += 12;
        } else if (period.toUpperCase() === 'AM' && hours === 12) {
            hours = 0;
        }
        return `${hours.toString().padStart(2, '0')}:${minutes}:00`;
    };

    const addCourse = () => {
        var selectedCourses = Array.from(document.querySelectorAll('.check:checked')).map((checkbox) => checkbox.id);
        const existingCourses = cookies.load('selectedCourses') || [];
        selectedCourses = selectedCourses.filter(course => !existingCourses.includes(course));
        const allCourses = existingCourses.concat(selectedCourses);

        console.log(selectedCourses);
        console.log(allCourses);
        if (selectedCourses.length === 0) {
            alert('No courses selected or already added');
            return;
        }
        fetch(`http://localhost:8080/api/isConflict?courses=${allCourses.join(',')}`)
        .then(response => response.json())
        .then(data => {
            if (data) {
                console.log(data);
                if (window.confirm("Some courses conflict. You will only be able to generate schedules. ")) {
                    cookies.save('selectedCourses', allCourses);
                    alert('Courses added successfully');
                } else {
                    alert('Courses not added');
                }
            } else {
                cookies.save('selectedCourses', allCourses);
                alert('Courses added successfully');
            }
            })
        .catch(error => console.error('Error fetching data:', error))
    }

    const validateTime = (time) => {
        return convertToMilitaryTime(time);
    }

    return (
        <div>
            <Form id="addCourseForm">
                    <FormControl
                        type="text"
                        placeholder="Search Courses"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className='searchBar'
                    />
                    <Button className='filterButton' onClick={() => setShowFilterForm(!showFilterForm)}>
                        Filters
                    </Button>
            </Form>
            {showFilterForm && (
                <Form className="filterForm">
                    <Form.Group className="filterGroup">
                        <Form.Label>Department:</Form.Label>
                        <FormControl as="select" value={selectedDepartment} onChange={(e) => setSelectedDepartment(e.target.value)}>
                            <option selected="selected" value="">All</option>
                            <option value="ACCT">Accounting</option>
                            <option value="ART">Art</option>
                            <option value="ASTR">Astronomy</option>
                            <option value="BIBL">Bible</option>
                            <option value="BIOL">Biology</option>
                            <option value="CHEM">Chemistry</option>
                            <option value="CMIN">Christian Ministries</option>
                            <option value="COMM">Communication</option>
                            <option value="COMP">Computer Science</option>
                            <option value="DSCI">Data Science</option>
                            <option value="DESI">Design</option>
                            <option value="ECON">Economics</option>
                            <option value="EDUC">Education</option>
                            <option value="EDRS">Education, Reading Specialist</option>
                            <option value="ELEE">Electrical Engineering</option>
                            <option value="ENGR">Engineering</option>
                            <option value="ENGL">English</option>
                            <option value="ENTR">Entrepreneurship</option>
                            <option value="EXER">Exercise Science</option>
                            <option value="FNCE">Finance</option>
                            <option value="FYSE">First Year Seminar</option>
                            <option value="FREN">French</option>
                            <option value="GEOL">Geology</option>
                            <option value="GOBL">Global Studies</option>
                            <option value="GREK">Greek</option>
                            <option value="HEBR">Hebrew</option>
                            <option value="HIST">History</option>
                            <option value="HUMA">Humanities</option>
                            <option value="INBS">International Business</option>
                            <option value="LATN">Latin</option>
                            <option value="LEGL">Legal Studies</option>
                            <option value="MNGT">Management</option>
                            <option value="MARK">Marketing</option>
                            <option value="MATH">Mathematics</option>
                            <option value="MECE">Mechanical Engineering</option>
                            <option value="MUSI">Music</option>
                            <option value="MUSE">Music Education</option>
                            <option value="NURS">Nursing</option>
                            <option value="PHIL">Philosophy</option>
                            <option value="PHYE">Physical Education</option>
                            <option value="PHYS">Physics</option>
                            <option value="POLS">Political Science</option>
                            <option value="PSYC">Psychology</option>
                            <option value="PUBH">Public Health</option>
                            <option value="RELI">Religion</option>
                            <option value="ROBO">Robotics</option>
                            <option value="SCIC">Science</option>
                            <option value="SSFT">Science, Faith &amp; Technology</option>
                            <option value="SOCW">Social Work</option>
                            <option value="SOCI">Sociology</option>
                            <option value="SPAN">Spanish</option>
                            <option value="SEDU">Special Education</option>
                            <option value="STAT">Statistics</option>
                            <option value="ABRD">Study Abroad</option>
                            <option value="SYSE">Systems Engineering</option>
                            <option value="THEA">Theatre</option>
                            <option value="WRIT">Writing</option>
                        </FormControl>
                    </Form.Group>
                    <Form.Group className="filterGroup">
                        <Form.Label>Professor:</Form.Label>
                        <FormControl
                            type="text"
                            placeholder="Enter Professor Name"
                            value={professor}
                            onChange={(e) => setProfessor(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="filterGroup">
                        <Form.Label>No Classes Before:</Form.Label>
                        <FormControl
                            type="text"
                            placeholder="HH:MM (AM/PM)"
                            value={startTime}
                            onChange={(e) => setStartTime(e.target.value)}
                            isInvalid={!validateTime(startTime)}
                        />
                    </Form.Group>
                    <Form.Group className="filterGroup">
                        <Form.Label>No Classes After:</Form.Label>
                        <FormControl
                            type="text"
                            placeholder="HH:MM (AM/PM)"
                            value={endTime}
                            onChange={(e) => setEndTime(e.target.value)}
                            isInvalid={!validateTime(endTime)}
                        />
                    </Form.Group>
                </Form>
            )}
            <ul className='scrollList'>
                {filteredResults.map((course) => (
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
            <Button variant="primary" onClick={addCourse}>
            Add Courses </Button>
        </div>
    );
}

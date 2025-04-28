// This component is responsible for displaying the search bar and the list of courses that can be added to the schedule.
// Page feature added by AI.
import { useEffect, useState } from 'react';
import { Form, FormControl, Button } from 'react-bootstrap';
import cookies from 'react-cookies';
import { Navigate } from 'react-router-dom';

export const sortDays = (days) => {
    const dayOrder = ['M', 'T', 'W', 'R', 'F'];
    return days.sort((a, b) => dayOrder.indexOf(a) - dayOrder.indexOf(b));
};

const convertToNormalTime = (time) => {
    const [hours, minutes] = time.split(':');
    let normalHours = parseInt(hours, 10);
    const period = normalHours >= 12 ? 'PM' : 'AM';
    normalHours = normalHours % 12 || 12; // Convert 0 to 12 for 12 AM
    return `${normalHours}:${minutes}${period}`;
};

export const groupTimes = (times) => {
    const grouped = {};
    for (const [day, time] of Object.entries(times)) {
        const timeKey = `${convertToNormalTime(time[0])} - ${convertToNormalTime(time[1])}`;
        if (!grouped[timeKey]) {
            grouped[timeKey] = [];
            grouped[timeKey] = [];
        }
        grouped[timeKey].push(day);
    }
    return grouped;
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
    const [semester, setSemester] = useState('2025_Spring');
    const [currentPage, setCurrentPage] = useState(1);
    const [showTimeBlockForm, setShowTimeBlockForm] = useState(false);
    const [selectedDays, setSelectedDays] = useState([]);
    const [alreadySelected, setAlreadySelected] = useState([]);
    const [checkmarkedResults, setCheckmarkedResults] = useState([]);
    const [checkmarkedData, setCheckmarkedData] = useState([]);
    const [shouldRedirect, setShouldRedirect] = useState(false);
    const itemsPerPage = 20;
    var counter = 0;

    useEffect(() => {
        updateResults();
    }, [selectedDepartment, professor, startTime, endTime, searchQuery, selectedDays]);

    const updateResults = () => {
        var sTime = '00:00:00';
        var eTime = '23:59:59';
        var days = ['M', 'T', 'W', 'R', 'F'];
        if (selectedDays.length > 0) {
            console.log(selectedDays);
            days = selectedDays;
        }
        if (convertToMilitaryTime(startTime) != null) {
            sTime = convertToMilitaryTime(startTime);
        }
        if (convertToMilitaryTime(endTime) != null) {
            eTime = convertToMilitaryTime(endTime);
        }

        const query = searchQuery ? `/${searchQuery}` : '';
        const url = `http://localhost:8080/api/search${query}?department=${selectedDepartment}&professor=${professor}&days=${days.join(',')}&times=${sTime}-${eTime}`;
        setFilteredResults([]);
        setCurrentPage(1);

        if (checkmarkedResults.length > 0) {
            fetch(`http://localhost:8080/api/coursesInfo?courses=${checkmarkedResults.join(',')}`)
                .then(response => response.json())
                .then(data => {
                    setCheckmarkedData(data);
                })
                .catch(error => console.error('Error fetching data:', error));
        } else {
            setCheckmarkedData([]);
        }
        if (cookies.load('selectedCourses').length > 0) {
            fetch(`http://localhost:8080/api/coursesInfo?courses=${cookies.load('selectedCourses').join(',')}`)
                .then(response => response.json())
                .then(data => {
                    setAlreadySelected(data);
                })
                .catch(error => console.error('Error fetching data:', error));
        } else {
            setAlreadySelected([]);
        }

        fetch(url)
            .then(response => response.json())
            .then(data => {
                const selectedCourses = cookies.load('selectedCourses') || [];
                const filteredData = data.filter(course =>
                    !selectedCourses.includes(`${course.department}${course.courseCode}${course.section}${course.semester}`)
                    && !checkmarkedResults.includes(`${course.department}${course.courseCode}${course.section}${course.semester}`)
                );
                setResults(data);
                setFilteredResults(filteredData);
            })
            .catch(error => console.error('Error fetching data:', error));
    }

    // generated with AI
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

    const handleDayChange = (day) => {
        setSelectedDays((prevDays) =>
            prevDays.includes(day)
                ? prevDays.filter((d) => d !== day)
                : [...prevDays, day]
        );
    };

    const addCourse = () => {
        var selectedCourses = Array.from(document.querySelectorAll('.check:checked')).map((checkbox) => checkbox.id);
        const existingCourses = cookies.load('selectedCourses') || [];
        selectedCourses = selectedCourses.filter(course => !existingCourses.includes(course));
        const allCourses = existingCourses.concat(selectedCourses);

        if (existingCourses.length >= 10) {
            alert('You cannot add more than 10 courses.');
            return;
        }

        if (allCourses.length > 10) {
            alert('You cannot add more than 10 courses.');
            return;
        }

        for (let element of Array.from(document.querySelectorAll('.check:checked'))) {
            element.checked = false;
            toggleRowHighlight();
        }

        // Extract course IDs (e.g., "COMP141") from the selected courses
        const courseIds = allCourses.map(course => course.match(/^[A-Z]+[0-9]+/)[0]);

        // Check for duplicate course IDs
        const uniqueCourseIds = new Set(courseIds);
        if (uniqueCourseIds.size !== courseIds.length) {
            alert("Cannot add same class twice");
            return;
        }

        if (selectedCourses.length === 0) {
            alert('No courses selected or already added');
            return;
        }
        fetch(`http://localhost:8080/api/isConflict?courses=${allCourses.join(',')}`)
        .then(response => response.json())
        .then(data => {
            if (data) {
                if (window.confirm("Some courses conflict. Generate schedules with these courses? ")) {
                    cookies.save('generateCourseList', allCourses);
                    setShouldRedirect(true);
                } else {
                    alert('Courses not added');
                }
            } else {
                cookies.save('generateCourseList', allCourses);
                cookies.save('selectedCourses', allCourses);
                alert('Courses added successfully');
            }
        })
        .catch(error => console.error('Error fetching data:', error))
    }

    const validateTime = (time) => {
        return convertToMilitaryTime(time);
    }

    const paginate = (data, page, itemsPerPage) => {
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        return data.slice(start, end);
    }

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
    }

    const combinedResults = [...filteredResults];

    const paginatedResults = paginate(combinedResults, currentPage, itemsPerPage);

    const toggleRowHighlight = () => {
        let ids = [];
        for (let element of document.querySelectorAll('.check:checked')) {
            ids.push(element.id);
        }
        setCheckmarkedResults(ids);
        for (let element of document.querySelectorAll('tr')) {
            console.log(element.querySelectorAll('.check').length === 0);
            if (element.querySelectorAll('.check').length > 0 && ids.includes(element.id)) {
                element.className = 'highlighted-row pointer';
            } else {
                if (element.className === 'alreadySelected') {
                    continue;
                } else {
                    if (element.className === 'highlighted-row pointer' || element.className === 'pointer') {
                        element.className = 'pointer';
                    } else {
                        element.className = '';
                    }
                }
            }
        }
    };

    if (shouldRedirect) {
        return <Navigate to="/generate" replace />;
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
                    <Button className='filterButton button' onClick={() => setShowFilterForm(!showFilterForm)}>
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
                            <Form.Label>No Classes Before: </Form.Label>
                            <FormControl
                                type="text"
                                placeholder="HH:MM (AM/PM)"
                                value={startTime}
                                onChange={(e) => setStartTime(e.target.value)}
                                isInvalid={!validateTime(startTime)}
                            />
                        </Form.Group>
                        <Form.Group className="filterGroup">
                            <Form.Label>No Classes After: </Form.Label>
                            <FormControl
                                type="text"
                                placeholder="HH:MM (AM/PM)"
                                value={endTime}
                                onChange={(e) => setEndTime(e.target.value)}
                                isInvalid={!validateTime(endTime)}
                            />
                        </Form.Group>
                        <Form.Group className="daysFilter">
                            {['M', 'T', 'W', 'R', 'F'].map((day) => (
                                <Form.Check
                                    key={day}
                                    type="checkbox"
                                    label={day}
                                    checked={selectedDays.includes(day)}
                                    onChange={() => handleDayChange(day)}
                                />
                            ))}
                        </Form.Group>
                    </Form>
                )}
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
                        {alreadySelected.map((course) => (
                            <tr key={`${course.department}${course.courseCode}${course.section}${course.semester}`} id={`${course.department}${course.courseCode}${course.section}${course.semester}`} className='alreadySelected'>
                                <td></td>
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
                        {checkmarkedData.map((course) => (
                            <tr
                                key={`${course.department}${course.courseCode}${course.section}${course.semester}`}
                                id={`${course.department}${course.courseCode}${course.section}${course.semester}`}
                                className="highlighted-row pointer"

                                onClick={(e) => {
                                    if (!e.target.classList.contains('check')) {
                                        const checkbox = e.currentTarget.querySelector(`#${course.department}${course.courseCode}${course.section}${course.semester}`);
                                        checkbox.click();
                                    }
                                    toggleRowHighlight();
                                }}
                            >
                                    <td><input type="checkbox" className='check' defaultChecked={true} id={`${course.department}${course.courseCode}${course.section}${course.semester}`} onClick={ toggleRowHighlight }></input></td>
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
                        {paginatedResults.map((course) => (
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
                <div className="pagination">
                    <Button className="button" onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>Previous</Button>
                    <span>Page {currentPage}</span>
                    <Button className="button" onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage * itemsPerPage >= filteredResults.length}>Next</Button>
                </div>
                <Button className='addButton button' variant="primary" onClick={addCourse}>Add Courses</Button>
            </div>
        );
}

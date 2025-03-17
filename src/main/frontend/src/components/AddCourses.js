import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import { Form, FormControl, Button } from 'react-bootstrap';
import cookies from 'react-cookies';

const AddCourses = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [results, setResults] = useState([]);
    const [filteredResults, setFilteredResults] = useState([]);

    useEffect(() => {
        if (searchQuery === '') {
            return;
        }
        setResults(
            fetch(`http://localhost:8080/api/search/${searchQuery}`)
            .then(response => response.json())
            .then(data => {
                setResults(data);
                setFilteredResults(data);
                console.log(data);
            })
            .catch(error => console.error('Error fetching data:', error))
        );
    }, [searchQuery]);

    return (
        <div>
            <Form>
                <FormControl
                    type="text"
                    placeholder="Search Courses"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </Form>
            <ul>
                {filteredResults.map((course) => (
                    <li key={`${course.department}${course.courseCode}`}>{course.department}{course.courseCode}</li>
                ))}
            </ul>
        </div>
    );
}

export default AddCourses;
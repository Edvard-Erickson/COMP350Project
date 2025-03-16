import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import { Form, FormControl, Button } from 'react-bootstrap';
import cookies from 'react-cookies';

const SettingsPage = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [setResults, setResults] = useState([]);
    const [setFilteredResults, setFilteredResults] = useState([]);

    useEffect(() => {
            setResults(
                courses.filter(course =>
                    course.toLowerCase().includes(searchQuery.toLowerCase())
                )
            );
        }, [searchQuery]);

    return (
        <div>

        </div>
    );
}
import { useEffect, useState } from 'react'
import { Link } from "react-router-dom"
import FullCalendar from '@fullcalendar/react'
import timeGridPlugin from '@fullcalendar/timegrid'
import cookies from 'react-cookies'

const dayMapping = {
    'M': 1,
    'T': 2,
    'W': 3,
    'R': 4,
    'F': 5,
    'S': 6,
    'U': 0
};

const Schedule = () => {
    const [hasConflict, setHasConflict] = useState(false);
    const [courses, setCourses] = useState([]);

    useEffect(() => {
        const selectedCourses = cookies.load('selectedCourses') || [];
        if (selectedCourses.length > 0) {
            fetch(`http://localhost:8080/api/isConflict?courses=${selectedCourses.join(',')}`)
                .then(response => response.json())
                .then(data => {
                    setHasConflict(data);
                    if (!data) {
                        fetch(`http://localhost:8080/api/coursesInfo?courses=${selectedCourses.join(',')}`)
                        .then(response => response.json())
                        .then(data => {
                            setCourses(data);
                            console.log(data);
                        })
                        .catch(error => console.error('Error fetching data:', error));
                    }
                })
                .catch(error => console.error('Error fetching data:', error));
        }
    }, []);

    if (hasConflict) {
        return (
            <div>
            <button onClick={handleFileLoad} class="scheduleButton">Load File</button>
            <h2>There is a conflict in your schedule</h2>;
            </div>
        );
    }

    const handleDownload = () => {
        const filename = "your_filename.txt";
        const coursesData = JSON.stringify(courses);

        fetch(`http://localhost:8080/download?filename=${filename}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: coursesData
        })
        .then(response => {
            if (response.ok) {
                return response.blob();
            }
            throw new Error('Network response was not ok.');
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => console.error('Error downloading file:', error));
    };

    const handleFileLoad = (event) => {
        const file = event.target.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);

            fetch('http://localhost:8080/upload', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                console.log('File uploaded successfully:', data);
            })
            .catch(error => console.error('Error uploading file:', error));
        }
    };

    return (
        <div>
            <button onClick={handleDownload} class="scheduleButton">Save As</button>
            <button onClick={handleFileLoad} class="scheduleButton">Load File</button>
        <FullCalendar
            height='auto'
            expandRows={true}
            plugins={[timeGridPlugin]}
            initialView="timeGridWeek"
            showNonCurrentDates={false}
            dayHeaderFormat = {{
                weekday: 'long',
                month: undefined,
                day: undefined
            }}
            weekends={false}
            allDaySlot={false}
            headerToolbar={false}
            events={courses.flatMap(course =>
                Object.entries(course.times).map(([day, time]) => ({
                    eventDisplay: 'block',
                    displayEventTime: false,
                    title: `${course.department}${course.courseCode}#${course.section}`,
                    daysOfWeek: [dayMapping[day]],
                    startTime: time[0],
                    endTime: time[1],
                    startRecur: '2023-01-01',
                    endRecur: '2027-12-31'
                }))
            )}
            eventColor='#444'
            slotMinTime='06:00:00'
            slotMaxTime='22:00:00'
        />
    </div>
    );
}

export default Schedule;
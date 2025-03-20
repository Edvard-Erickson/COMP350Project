import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import cookies from 'react-cookies';

const dayMapping = {
    'M': 1,
    'T': 2,
    'W': 3,
    'R': 4,
    'F': 5,
    'S': 6,
    'U': 0
};

const Generate = () => {
    const [hasConflict, setHasConflict] = useState(false);
    const [schedules, setSchedules] = useState([]);
    const [currentScheduleIndex, setCurrentScheduleIndex] = useState(0);

    useEffect(() => {
        const selectedCourses = cookies.load('selectedCourses') || [];
        if (selectedCourses.length > 0) {
            fetch(`http://localhost:8080/api/generate?courses=${selectedCourses.join(',')}`)
            .then(response => response.json())
            .then(data => {
                setSchedules(data);
                console.log("DATA: ", data);
            })
            .catch(error => console.error('Error fetching data:', error));
        }
    }, []);

    const handleNextSchedule = () => {
        setCurrentScheduleIndex((prevIndex) => (prevIndex + 1) % schedules.length);
    };

    const handlePreviousSchedule = () => {
        setCurrentScheduleIndex((prevIndex) => (prevIndex - 1 + schedules.length) % schedules.length);
    };

    const handleSetMainSchedule = () => {
        const currentSchedule = schedules[currentScheduleIndex];
        const courseIds = currentSchedule.map(course => `${course.department}${course.courseCode}${course.section}${course.semester}`);
        cookies.save('selectedCourses', courseIds, { path: '/' });
        alert('Main schedule set successfully!');
    };

    if (schedules.length === 0) {
        return <h2>Loading schedules...</h2>;
    }

    const currentSchedule = schedules[currentScheduleIndex];

    return (
        <div>
            <button onClick={handlePreviousSchedule} className="generateButton">Previous</button>
            <span>{currentScheduleIndex + 1} / {schedules.length}</span>
            <button onClick={handleNextSchedule} className="generateButton">Next</button>
            <button onClick={handleSetMainSchedule} className="generateButton">Set Main Schedule</button>
            <FullCalendar
                height='auto'
                expandRows={true}
                plugins={[timeGridPlugin]}
                initialView="timeGridWeek"
                showNonCurrentDates={false}
                dayHeaderFormat={{
                    weekday: 'long',
                    month: undefined,
                    day: undefined
                }}
                weekends={false}
                allDaySlot={false}
                headerToolbar={false}
                events={currentSchedule.flatMap(course =>
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
};

export default Generate;
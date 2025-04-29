// this component handles the generate schedules page, getting schedules from the backend and displaying them.
import { useEffect, useState } from 'react';
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
    const [noSchedules, setNoSchedules] = useState(false);

    useEffect(() => {
        const selectedCourses = cookies.load('generateCourseList') || [];
        if (selectedCourses.length > 0) {
            fetch(`http://localhost:8080/api/generate?courses=${selectedCourses.join(',')}`)
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    setNoSchedules(true);
                } else {
                    setSchedules(data);
                    setNoSchedules(false);
                }
                console.log("DATA: ", data);
            })
            .catch(error => console.error('Error fetching data:', error));
        } else {
            setNoSchedules(true);
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
        cookies.save('generateCourseList', courseIds);
        cookies.save('selectedCourses', courseIds);
        alert('Main schedule set successfully!');
    };

     if (noSchedules) {
            return <h2>No Courses Selected / No Schedules Returned</h2>;
        }

    if (schedules.length === 0) {
        return <h2>Loading schedules...</h2>;
    }

    const currentSchedule = schedules[currentScheduleIndex];

    return (
        <div>
            <button onClick={handlePreviousSchedule} className="generateButton button">Previous</button>
            <span>{currentScheduleIndex + 1} / {schedules.length}</span>
            <button onClick={handleNextSchedule} className="generateButton button">Next</button>
            <button onClick={handleSetMainSchedule} className="generateButton button">Set Main Schedule</button>
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
                eventColor='#333'
                slotMinTime='06:00:00'
                slotMaxTime='22:00:00'
            />
        </div>
    );
};

export default Generate;
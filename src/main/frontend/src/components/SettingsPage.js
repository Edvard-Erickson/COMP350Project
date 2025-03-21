// handles the settings page, which for now handles setting a list of majors.
import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import cookies from 'react-cookies';

const SettingsPage = () => {
    const [selectValue, setSelectValue] = useState('');
    const [selectRemoveValue, setSelectRemoveValue] = useState('');
    const [selectedPrograms, setSelectedPrograms] = useState([]);

    useEffect(() => {
        const savedPrograms = cookies.load('programs');
        if (savedPrograms) {
            setSelectedPrograms(savedPrograms);
        }
    }, []);

    const handleChange = (event) => {
        setSelectValue(event.target.value);
    }

    const handleRemoveChange = (event) => {
            setSelectRemoveValue(event.target.value);
        }

    useEffect(() => {
        if (selectedPrograms.length > 0) {
            cookies.save('programs', selectedPrograms, { path: '/' });
            console.log('Programs saved in cookies:', cookies.load('programs'));
        }
    }, [selectedPrograms]);

    const handleSubmit = (event) => {
         event.preventDefault();
         if (!selectValue) {
            setSelectValue('');
            return;
         }
         const selectedOption = options.find(option => option.href === selectValue);
         const updatedPrograms = [...selectedPrograms, { name: selectedOption.name, href: selectedOption.href }];
         setSelectedPrograms(updatedPrograms);
         setSelectValue('');
         cookies.save('programs', updatedPrograms, { path: '/' });
         console.log('Programs saved in cookies:', cookies.load('programs'));
    }

    const removeProgram = (event) => {
        event.preventDefault();
        if (!selectRemoveValue) {
            setSelectRemoveValue('')
            return;
        }
        const selectedOption = options.find(option => option.href === selectRemoveValue);
        const updatedPrograms = selectedPrograms.filter(program => program.href !== selectedOption.href);
        setSelectedPrograms(updatedPrograms);
        setSelectRemoveValue('');
        cookies.save('programs', updatedPrograms, { path: '/' });
        console.log('Programs saved in cookies:', cookies.load('programs'));
    }

    var options = getAllPrograms();
    var filteredOptions = options.filter(option => !selectedPrograms.some(program => program.href === option.href));
    return (
        <div>
            <h2>Settings</h2>
            <p>Current Programs: {selectedPrograms.map(program => program.name).join(', ')}</p>
            <p>ADD A PROGRAM:</p>
            <form id="settingsForm" onSubmit={handleSubmit}>
                <select id="programs" value={ selectValue } onChange={ handleChange }>
                    <option value="" disabled>Select...</option>
                    {filteredOptions.map((option) => (
                      <option key={option.name} value={option.href}>
                        {option.name}
                      </option>
                    ))}
                </select>
                <button type="submit" className="button">Add Program</button>
            </form>
            <p>REMOVE A PROGRAM:</p>
            <form id="settingsForm" onSubmit={removeProgram}>
                <select id="programs" value={ selectRemoveValue } onChange={ handleRemoveChange }>
                    <option value="" disabled>Select...</option>
                    {selectedPrograms.map((option) => (
                      <option key={option.name} value={option.href}>
                        {option.name}
                      </option>
                    ))}
                </select>
                <button type="submit" className="button">Remove Program</button>
            </form>
        </div>
    )
}

function getAllPrograms() {
    return [
            { name: "Accounting", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Accounting_2028.pdf"},
            { name: "Accounting - Business Analysis 150 hr. dual major w/ CPA Concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Accounting_BusaAnalysis_2028.pdf"},
            { name: "Accounting - Corporate and Not for Profit", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Accounting_Corporateandnotforprofit_2028.pdf"},
            { name: "Accounting - Finance 150 hr. dual major w/ CPA Concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Accounting_Finance_2028.pdf"},
            { name: "Accounting - CPA Concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Accounting_CPA_2028.pdf"},
            { name: "Finance", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Finance_2028.pdf"},
            { name: "Applied Science and Engineering with Biomedical Engr concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/AppliedScienceandEngineering_BiomedicalEngrConc_2028.pdf"},
            { name: "Applied Science and Engineering with Public Health Concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/AppliedScienceandEngineering_PublicHealthConc_2028.pdf"},
            { name: "Applied Science and Engineering with Technology Management Concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/AppliedScienceandEngineering_TechMngtConc_2028.pdf"},
            { name: "Biblical & Religious Studies", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BiblicalandReligiousStudies_2028.pdf"},
            { name: "Philosophy", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Philosophy_2028.pdf"},
            { name: "Christian Ministries", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ChristianMinistries_2028.pdf"},
            { name: "Biblical and Religious Studies and M.A. in Theology and Ministry", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BiblicalandReligiousStudies_TheologyandMinistry_2028.pdf"},
            { name: "Biology", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Biology_2028.pdf"},
            { name: "Conservation Biology", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ConservationBiology_2028.pdf"},
            { name: "Biology and General Science Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Biology_GenSciSecEduc_2028.pdf"},
            { name: "Molecular Biology", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MolecularBiology_2028.pdf"},
            { name: "Biology Health", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BiologyHealth_2028.pdf"},
            { name: "Biochemistry", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Biochemistry_2028.pdf"},
            { name: "Chemistry", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Chemistry_2028.pdf"},
            { name: "Biochemistry with Chemical Synthesis concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Biochemistry_ChemicalSynthesis_2028.pdf"},
            { name: "Chemistry and General Science Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Chemistry_GenSciSecEduc_2028.pdf"},
            { name: "Biochemistry with Forensic Chemistry concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Biochemistry_ForensicChemistry_2028.pdf"},
            { name: "Chemistry Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Chemistry_SecEduc_2028.pdf"},
            { name: "Biochemistry with Health concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Biochemistry_Health_2028.pdf"},
            { name: "Communication Arts", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/CommunicationArts_2028.pdf"},
            { name: "Design &amp; Innovation", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/DesignandInnovation_2028.pdf"},
            { name: "Computer Science (B.S.)", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ComputerScience_BS_%202028.pdf"},
            { name: "Computer Programming", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ComputerProgramming_2028.pdf"},
            { name: "Data Science", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/DataScience_2028.pdf"},
            { name: "Economics", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Economics_2028.pdf"},
            { name: "Business Economics", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BusinessEconomics_2028.pdf"},
            { name: "Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ElementaryEduc_2028.pdf"},
            { name: "Middle Level Math-English Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_MathEnglEduc_2028.pdf"},
            { name: "Middle Level Math-English and Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_MathEnglandElemEduc_2028.pdf"},
            { name: "Middle Level Math-History Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_MathHistEduc_2028.pdf"},
            { name: "Middle Level Science-English Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_ScienceEnglEduc_2028.pdf"},
            { name: "Middle Level Math-History and Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_MathHistandElemEduc_2028.pdf"},
            { name: "Middle Level Science-English and Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_ScienceEnglandElemEduc_2028.pdf"},
            { name: "Middle Level Science-History Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_ScienceHistEduc_2028.pdf"},
            { name: "Middle Level Science-History and Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_ScienceHistandElemEduc_2028.pdf"},
            { name: "Special Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/SpecialEducation_2028.pdf"},
            { name: "Middle Level Science-Math Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_ScienceMathEduc_2028.pdf"},
            { name: "Special Education with Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/SpecialEducwithElementaryEduc_2028.pdf"},
            { name: "Middle Level Science-Math and Elementary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MiddleLevel_ScienceMathandElemEduc_2028.pdf"},
            { name: "Electrical Engineering", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ElectricalEngineering_2028.pdf"},
            { name: "Computer Engineering", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ComputerEngineering_2028.pdf"},
            { name: "English", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/English_2028.pdf"},
            { name: "English and Communication Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/English_CommunicationSecondaryEducation_2028.pdf"},
            { name: "English Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/English_SecondaryEducation_2028.pdf"},
            { name: "Entrepreneurship", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Entrepreneurship_2028.pdf"},
            { name: "Exercise Science", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/ExerciseScience_%202028.pdf"},
            { name: "History", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/History_2028.pdf"},
            { name: "History and Social Studies Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/History_SocStudiesSecEd_2028.pdf"},
            { name: "Business Analysis", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BusinessAnalysis_2028.pdf"},
            { name: "Management", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Management_2028.pdf"},
            { name: "Business Statistics", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BusinessStatistics_2028.pdf"},
            { name: "Marketing", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Marketing_2028.pdf"},
            { name: "Human Resource Management", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/HumanResourceManagement_2028.pdf"},
            { name: "Supply Chain Management", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/SupplyChainManagement_2028.pdf"},
            { name: "International Business", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/InternationalBusiness_2028.pdf"},
            { name: "Mathematics", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Mathematics_2028.pdf"},
            { name: "Mathematics Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Mathematics_SecEduc_2028.pdf"},
            { name: "Mathematics with Actuarial Science concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Mathematics_ActuarialScience_2028.pdf"},
            { name: "Applied Statistics", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/AppliedStatistics_2028.pdf"},
            { name: "Mechanical Engineering", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MechanicalEngineering_2028.pdf"},
            { name: "French", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/French_2028.pdf"},
            { name: "Spanish", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Spanish_2028.pdf"},
            { name: "French K-12 Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/French_K12Educ_2028.pdf"},
            { name: "Spanish K-12 Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Spanish_K12Educ_2028.pdf"},
            { name: "Music", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Music_2028.pdf"},
            { name: "Music Performance - Instrumental", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MusicPerformance_Instrumental_2028.pdf"},
            { name: "Music Business", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MusicBusiness_2028.pdf"},
            { name: "Music Performance - Piano", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MusicPerformance_Piano_2028.pdf"},
            { name: "Music PreK-12 Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MusicPreK12Educ_2028.pdf"},
            { name: "Music Performance - Vocal", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MusicPerformance_Vocal_2028.pdf"},
            { name: "Music Religion", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/MusicReligion_2028.pdf"},
            { name: "Nursing", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Nursing_2028.pdf"},
            { name: "Physics", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Physics_2028.pdf"},
            { name: "Physics and General Science Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Physics_GenSciSecEduc_2028.pdf"},
            { name: "Physics with Computer Hardware concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Physics_ComputerHardware_2028.pdf"},
            { name: "Physics Secondary Education", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Physics_%20SecEduc_2028.pdf"},
            { name: "Physics with Computer Software concentration", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Physics_ComputerSoftware_2028.pdf"},
            { name: "Political Science", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/PoliticalScience_2028.pdf"},
            { name: "Psychology (B.A.)", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Psychology_%20BA_2028.pdf"},
            { name: "Social Work", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/SocialWork_2028.pdf"},
            { name: "Psychology (B.S.)", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Psychology_BS_2028.pdf"},
            { name: "Undeclared", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/Undeclared_2028.pdf"},
            { name: "M.S. in Accounting", href: "https://www.gcc.edu/Portals/0/Grad-Program-Guide_MAcc.pdf"},
            { name: "Master of Business Administration", href: "https://www.gcc.edu/Portals/0/Grad-Program-Guide_MBA.pdf"},
            { name: "M.S. in Business Analytics", href: "https://www.gcc.edu/Portals/0/Grad-Program-Guide_MSBA.pdf"},
            { name: "M.A. in Economics", href: "https://www.gcc.edu/Portals/0/Grad-Program-Guide_Econ-MA.pdf"},
            { name: "M.S. in Kinesiology", href: "https://www.gcc.edu/Portals/0/Grad-Program-Guide_MKIN.pdf"},
            { name: "M.A. in Theology & Ministry", href: "https://my.gcc.edu/docs/registrar/programguides/statussheets/2024/BiblicalandReligiousStudies_TheologyandMinistry_2028.pdf"}]
}

export default SettingsPage;
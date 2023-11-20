import Skill from './Skill';
import GrayBox from '../GrayBox';
import { useState } from 'react';
import { Button } from "@nextui-org/react";
import { toast } from 'react-toastify';

export default function SkillList({ list, topic, username, onOpen, setModalState }) {
	let [ skills, setSkills ] = useState(list);
	async function addSkill(event) {
    	event.preventDefault();
    	const form = new FormData(event.target);
    	const formData = Object.fromEntries(form.entries());
    	const newSkill = {
    		...formData,
    		username: username
    	}

    	const response = await fetch('/api/add-skill', {
	      method: 'POST',
	      headers: {
	        "Content-Type": "application/json",
	      }, body: JSON.stringify({skill: newSkill, url: `${topic.api_url}${topic.post}`})
	    });

	    if (!response.ok) {
	    	toast("Bir hata oluştu :(");
	    	return;
	    }
	    toast("Başarıyla eklendi");

    	const skillAlreadyExists = skills.map(skill => skill[topic.primary]).includes(newSkill[topic.primary]);
    	const newSkills = (skillAlreadyExists
    		? skills.filter(skill => (skill[topic.primary] != newSkill[topic.primary]))
    		: [...skills]
    	);
    	newSkills.push(newSkill);
		setSkills(newSkills);

	}
	async function deleteSkill(event) {
    	event.preventDefault();
		setSkills(skills.filter(skill => (skill[topic.primary] != key)))
	}
	return (
		<GrayBox key={topic.key} className="mb-6 w-54">
			<h3 class="object-top text-lg"> {topic.title} </h3>
			{ skills.map(skill => Skill({skill: skill, topic: topic, deleteSelf: () => (deleteSkill(skill[topic.primary]))})) }
			<Button
				onPress={() => { setModalState({...topic, addSkill:addSkill}); onOpen() }}
			    className="mx-auto block mt-2 text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-bold rounded-full h-6 text-center dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">
			    +
			</Button>
		</GrayBox>
	);
}
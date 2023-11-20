import Skill from './Skill';
import GrayBox from '../GrayBox';
import { useState } from 'react';
import { Button } from "@nextui-org/react";

export default function SkillList({ list, topic, username, onOpen }) {
	let [ skills, setSkills ] = useState(list);
	async function addSkill(event) {
    	event.preventDefault();
		setSkills([...skills, elem]);
	}
	async function deleteSkill(event) {
    	event.preventDefault();
		setSkills(skills.filter(skill => (skill[topic.primary] != key)))
	}
	return (
		<GrayBox key={topic.key} className="mb-6 w-54">
			<h3 class="object-top text-lg"> {topic.title} </h3>
			{ skills.map(skill => Skill({skill: skill, topic: topic, deleteSelf: () => (deleteSkill(skill[topic.primary]))})) }
			<Button onPress={onOpen} className="mx-auto block mt-2 text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-bold rounded-full h-6 text-center dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">+</Button>
		</GrayBox>
	);
}
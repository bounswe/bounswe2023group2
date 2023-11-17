import Skill from './Skill';
import GrayBox from '../GrayBox';
import { useState } from 'react';

export default function SkillList({ list, topic, username }) {
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
			<input type="image" src="/icons/add.png" class="mx-auto w-5 mt-2 block" alt="Yeni girdi ekleyin"/>
		</GrayBox>
	);
}
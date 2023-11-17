import Skill from './Skill';
import GrayBox from '../GrayBox';
import { useState } from 'react';

export default function SkillList({ list, topic }) {
	let [ skills, setSkills ] = useState(list);
	function addSkill(skill) {
		setSkills([...skills, elem]);
	}
	function deleteSkill(key) {
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
import Skill from './Skill';

export default function SkillList({ list, topic }) {
	if (list.length === 0) {
		return (
			<div key={topic.key}>
				<h3 class="object-top text-lg"> {topic.title} </h3>
				<span class="text-sm text-gray-600"> Yok </span>
			</div>
		);
	}
	return list.map(elem => Skill({skill: elem, topic: topic}));
}
import Skill from './Skill';
import GrayBox from '../GrayBox';

export default function SkillList({ list, topic }) {
	return (
		<GrayBox key={topic.key} className="mb-6 w-48">
			<h3 class="object-top text-lg"> {topic.title} </h3>
			{list.length === 0 ?
				<span class="text-sm text-gray-600"> Yok </span>
				:
				list.map(elem => Skill({skill: elem, topic: topic}))
			}
		</GrayBox>
	);
}
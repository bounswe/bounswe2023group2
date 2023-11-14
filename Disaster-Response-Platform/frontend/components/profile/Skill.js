export default function Skill({ skill, topic }) {
	return (
		<div key={`${topic.key}-${skill[topic.primary]}`}>
			{
				topic.is_link ? 
				<a class="text-blue-600" href={skill[topic.url]}>{skill[topic.text]}</a> :
				<span class="text-center text-sm text-gray-600"> {skill[topic.primary]} (level: {skill[topic.secondary]}) </span>
			}
		</div>
	);
}
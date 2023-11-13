export default function Skill({ skill, topic }) {
	return (
		<div key={`${topic.key}-${skill[topic.primary]}`}>
			topic.is_link ? 
			<a href={social.profile_URL}>{platform_name}</a> :
			<span class="text-center text-sm text-gray-600"> {skill[topic.primary]} (level: {skill[topic.secondary]}) </span>
		</div>
	);
}
export default function Skill({ skill, topic, deleteSelf }) {
	return (
		<div key={`${topic.key}-${skill[topic.primary]}`} class="mt-1">
			{
				topic.is_link ? 
				<a class="text-blue-600 text-sm" href={skill[topic.secondary]}>{skill[topic.primary]}</a> :
				<span class="text-center text-sm text-gray-600"> {skill[topic.primary]} ({skill[topic.secondary]}) </span>
			}
			<form onSubmit={deleteSelf} class="float-right">
				<input type="image" src="/icons/remove.png" class="w-5 ml-2 my-auto align-middle" alt="Bu girdiyi silin"/>
			</form>
		</div>
	);
}
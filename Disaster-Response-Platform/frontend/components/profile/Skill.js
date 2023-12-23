import { PiCertificate } from "react-icons/pi";
import { Button } from "@nextui-org/react";

export default function Skill({ skill, topic, deleteSelf, noedit }) {
	return (
		<div key={`${topic.key}-${skill[topic.primary]}`} className="mt-1 flex items-center w-full">
				{
					topic.is_link ? 
					<a className="text-blue-600 text-sm mr-2 flex-grow" href={skill[topic.secondary]}>{skill[topic.primary]}</a> :
					<span className="text-sm text-gray-600 mr-2 flex-grow"> {skill[topic.primary]} ({skill[topic.secondary]}) </span>
				}
				{
					topic.certificate ? (
						<Button isIconOnly className="text-lg w-6 h-6 rounded-sm mx-2"> <PiCertificate /> </Button>
					) : null
				}
				{noedit ? null : 
					<form onSubmit={deleteSelf} className="ml-2">
						<input type="image" src="/icons/remove.png" className="w-5 my-auto block align-center" alt="Bu girdiyi silin"/>
					</form>
				}
		</div>
	);
}
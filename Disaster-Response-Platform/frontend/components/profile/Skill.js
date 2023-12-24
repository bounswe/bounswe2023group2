import { PiCertificate } from "react-icons/pi";
import { Link } from "@nextui-org/react";

export default function Skill({ skill, topic, deleteSelf, noedit }) {
	return (
		<div key={`${topic.key}-${skill[topic.primary]}`} className="mt-1 flex items-center w-full">
				{
					topic.is_link ? 
					<a className="text-blue-600 text-sm mr-2 flex-grow" href={skill[topic.secondary]}>{skill[topic.primary]}</a> :
					<span className="text-sm text-gray-600 mr-2 flex-grow"> {skill[topic.primary]} ({skill[topic.secondary]}) </span>
				}
				{
					skill[topic.certificate] ? (
						<Link
						      isExternal
						      showAnchorIcon
						      href={skill[topic.certificate]}
						      anchorIcon={<PiCertificate className="text-2xl"/>}
						    >
						    </Link>
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
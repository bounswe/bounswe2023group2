export default function GrayBox({ className, children }) {
	if (className != "") className += " ";
	className += "rounded-lg bg-gray-200 p-4";
	return (	
		<div class={className}>
			{children}
		</div>
	);
}
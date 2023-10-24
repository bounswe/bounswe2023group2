

export default function InfoField({ title, content }) {
	return (
		<div class="my-px">
			<span class="italic text-sm text-gray-600">{title}</span>
			<br />
			<span class="text-lg lg:text-xl">{content}</span>
		</div>
	)
}
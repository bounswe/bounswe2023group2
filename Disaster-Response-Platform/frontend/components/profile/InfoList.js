export default function InfoList({ list }) {
	if (list.length === 0) {
		return null;
	}
	return (
		<>
			<p> (TEMPORARY) {list} </p>
		</>
	);
}
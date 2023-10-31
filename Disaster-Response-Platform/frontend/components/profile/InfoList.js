export default function InfoList({ list }) {
	if (list.length === 0) {
		return null;
	}
	return list.entries().map(([index, elem]) => <div key={index.toString()}> elem </div>)
}
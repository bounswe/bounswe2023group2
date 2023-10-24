import InfoField from '../InfoField';
import GrayBox from '../GrayBox';

export default function ProfileInfo({ className, fields }) {
	return (
		<GrayBox className={className}>
			<h3 class="object-top text-center text-xl"> Optional Info </h3>
			{fields.map(InfoField)}
		</GrayBox>
	);
}
import InfoField from '../InfoField';
import GrayBox from '../GrayBox';

export default function OptionalInfo({ className, fields, labels }) {
	return (
		<GrayBox className={className}>
			<h3 class="object-top text-center text-xl"> {labels.profile_pages.info} </h3>
			{fields.map(InfoField)}
		</GrayBox>
	);
}
import Image from 'next/image';
import GrayBox from '../GrayBox';

export default function PictureAndMainInfo({ className, info }) {
	const {img, name, email, phone} = info;
	return (
		<GrayBox className={className}>
			<Image class="block mx-auto" src={img ? img : "/images/default_pp.jpg"} width={135} height={135}/>
			<div class="text-center">
				<span class="text-lg lg:text-xl">{name}</span>
				{email ? <> <br /> <span class="text-md lg:text-lg"> {email} </span> </> : null}
				{phone ? <> <br /> <span class="text-md lg:text-lg"> {phone} </span> </> : null}
			</div>
		</GrayBox>
	)
}
import fetchJson from '@/lib/fetchJson';
import '@/styles/globals.css'
import {NextUIProvider} from "@nextui-org/react";
import { SWRConfig } from 'swr';



export default function App({ Component, pageProps }) {
  const getLayout = Component.getLayout || ((page) => page);
  return ( 
    <SWRConfig
    value={{
      fetcher: fetchJson,
      onError: (err) => {
        console.error(err);
      },
    }}
  >
    <NextUIProvider>
      {getLayout( <Component { ...pageProps}  /> )}
    </NextUIProvider>
    </SWRConfig>
    
    )
}

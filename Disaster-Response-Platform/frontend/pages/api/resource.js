import { api } from '@/lib/apiUtils';

async function resourceRoute(req, res) {
    try{
        const { data } = await api.get('/resource');
    
        if(data?.payload?.success){
            res.status(200).json(data);
        }else{
            res.status(400).json(data);
        } 
    } catch (error) {
        console.log('error', error);
        res.status(error?.response?.status ?? 403).json({ error });
    }

}
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import NewPop from './newPop';

export default function Home() {
  const [news, setNews] = useState(null);
  const [open, setOpen] = useState(false);


  const NewsCard = (newItem) => {
    let item = newItem.newItem
    return (<div className="max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700 m-5">
      <a href={item.url}>
        <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">{item.title}</h5>
      </a>
      <p className="mb-3 font-normal text-gray-700 dark:text-gray-400">{item.description}</p>
      <a href={item.url} className="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        Read more
        <svg aria-hidden="true" className="w-4 h-4 ml-2 -mr-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
      </a>
    </div>
    )

  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try{

      const data = {
        subject: event.target.subject.value,
        language: event.target.language.value,
        sortBy: event.target.sortBy.value,
      };
      const headers = {
        'Content-Type': 'application/json',
      };
      const response = await axios.get(`${process.env.NEXT_PUBLIC_BACKEND_URL}/news/?subject=${data.subject}&sortBy=${data.sortBy}&language=${data.language}`, headers);
      const result = await JSON.stringify(response.data);
      const newsObject = JSON.parse(result)
      console.log(newsObject.payload.articles)
      setNews(newsObject.payload.articles)
    }
    catch{
      window.alert("Error occured while fetching news")
    }
  };
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <button onClick={(e)=>{setOpen(true)}} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded "> Add new</button>
       { open && <NewPop  open={open} setOpen={setOpen}/>}
      <div className="flex  flex-row items-center justify-between ">

      
        <form onSubmit={handleSubmit}>
          <div className="inline-block relative w-64 mr-10">
            <select id='language' className="block appearance-none w-full border border-gray-400 text-black hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
              <option value="tr">Turkish</option>
              <option value="en">English</option>
              <option value="de">German</option>
            </select>
            <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
              <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" /></svg>
            </div>
          </div>

          <div className="inline-block relative w-64 mr-10">
            <input type='text' id='subject' className="block appearance-none w-full border border-gray-400 text-black hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
            </input>
          </div>

          <div className="inline-block relative w-64 mr-10">
            <select id='sortBy' className="block appearance-none w-full border border-gray-400 text-black hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
              <option value="relevancy">relevancy</option>
              <option value="popularity">popularity</option>
              <option value="publishedAt">published at</option>
            </select>
          </div>
          <button type='submit' className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded "> Get News</button>
        </form>
      </div>

      <div className="flex flex-row justify-center flex-wrap pt-20">
        {news == null ? <></> :
          news.map((newItem, index) => {
            return (
              <NewsCard key={index} newItem={newItem} ></NewsCard>
            );
          })}
      </div>


    </main>
  );
}


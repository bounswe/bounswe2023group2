import Skill from './Skill';
import GrayBox from '../GrayBox';
import { useState } from 'react';
import { Button } from "@nextui-org/react";
import { toast } from 'react-toastify';
import { api } from '@/lib/apiUtils';
import { uploadFile } from '@/lib/files';

export default function SkillList({ list, topic, username, onOpen, setModalState, noedit, wide, accessToken, labels }) {
  let [ skills, setSkills ] = useState(list);

  async function addSkill(event) {
    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());

    if (topic.certificate) {
      const file = document.getElementById("certificate")?.files?.[0];
      if (file) {
        const extension = file.name.substring(file.name.lastIndexOf(".")+1);
        const filename = `${topic.api_url}-${formData[topic.primary]}-certificate.${extension}`;
        const upload_response = await uploadFile(file, filename, accessToken);
        formData[topic.certificate] = upload_response?.data?.url;
      }
    }

    delete formData.certificate;
    
    const newSkill = {
      ...formData,
      username: username
    };

    const response = await fetch('/api/add-skill', {
      method: 'POST',
      headers: {
      "Content-Type": "application/json",
      }, body: JSON.stringify({skill: newSkill, url: `${topic.api_url}${topic.post}`})
    });

    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.add_success);

    const skillAlreadyExists = skills.map(skill => skill[topic.primary]).includes(newSkill[topic.primary]);
    const newSkills = (skillAlreadyExists
      ? skills.filter(skill => (skill[topic.primary] != newSkill[topic.primary]))
      : [...skills]
    );
    newSkills.push(newSkill);
    setSkills(newSkills);

  }
  
  async function deleteSkill(event, skill) {
    event.preventDefault();

    if (topic.certificate) {
      const filename = skill[topic.certificate].substring(skill[topic.certificate].lastIndexOf('/')+1);
      const upload_response = await fetch(`/api/delete-file/${filename}`, { method: 'DELETE' } );

      if (upload_response.status !== 200) {
        toast(labels.feedback.failure);
        return;
      }
    }

    const response = await fetch('/api/delete-skill', {
      method: 'POST',
      headers: {
      "Content-Type": "application/json",
      }, body: JSON.stringify({skill: skill, url: `${topic.api_url}${topic.delete}`})
    });

    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.remove_success);

    setSkills(skills.filter(other => (skill[topic.primary] != other[topic.primary])));
  }

  const skill_labels = labels.profile_lists[topic.api_url];

  if (noedit) {
    return (
      <GrayBox key={topic.key} className={wide ? "mb-6 w-64" : "mb-6 w-54"}>
        <h3 class="object-top text-lg"> {skill_labels.title} </h3>
        { skills.map(skill => Skill({skill, topic, noedit: true})) }
      </GrayBox>
    );
  }

  return (
    <GrayBox key={topic.key} className={wide ? "mb-6 w-64" : "mb-6 w-54"}>
      <h3 class="object-top text-lg"> {skill_labels.title} </h3>
      { skills.map(skill => Skill({skill, topic, deleteSelf: (event) => (deleteSkill(event, skill))})) }
      <Button
        onPress={() => { setModalState({...topic, addSkill}); onOpen() }}
        className="mx-auto block mt-2 text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-bold rounded-full h-6 text-center dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">
        +
      </Button>
    </GrayBox>
  );
}
/** @type {import('tailwindcss').Config} */
const {nextui} = require("@nextui-org/react");

module.exports = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './layouts/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
        'gradient-conic':
          'conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))',
      },
      colors: {
        action: {
          DEFAULT: '#994ad1',
          dark: '#761eb5'
        },
        emergency: {
          DEFAULT: '#ff0000',
          dark: '#a80c0c'
        },
        event: {
          DEFAULT: '#ffc107',
          dark: '#cd9b02'
        },
        need: {
          DEFAULT: '#00a9f6',
          dark: '#0677aa'
        },
        resource: {
          DEFAULT: '#00ba69',
          dark: '#067a47'
        },
        activity: {
          DEFAULT: '#515151',
          dark: '#656565'
        }
      }
    }
  },
  darkMode: "class",
  plugins: [
    nextui(),
  ],
}

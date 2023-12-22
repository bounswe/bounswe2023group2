/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'files.practice-app.online',
        port: '',
        pathname: '/**'
      }
    ]
  }
}

module.exports = nextConfig

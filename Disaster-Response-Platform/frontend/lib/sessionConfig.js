 const sessionConfig = {
  cookieName: 'cookie',
  password: 'complex_password_at_least_32_characters_long',
  // secure: true should be used in production (HTTPS) but can't be used in development (HTTP)
  cookieOptions: {
    secure: false,
    maxAge: 1000 * 60 * 60 * 24 * 30, // 30 days
  },
}
export default sessionConfig;
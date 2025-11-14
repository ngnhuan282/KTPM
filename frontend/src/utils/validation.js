export const validateEmail = (email) => /\S+@\S+\.\S+/.test(email)
export const validatePassword = (p) => p && p.length >= 5

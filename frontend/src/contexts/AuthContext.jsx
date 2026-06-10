import { createContext, useContext, useState, useCallback } from 'react'
import { login as loginApi } from '../api/axios'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [member, setMember] = useState(() => {
    const saved = localStorage.getItem('member')
    return saved ? JSON.parse(saved) : null
  })

  const login = useCallback(async (username, password) => {
    const { token, member: memberData } = await loginApi(username, password)
    localStorage.setItem('token', token)
    localStorage.setItem('member', JSON.stringify(memberData))
    setMember(memberData)
    return memberData
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('member')
    setMember(null)
  }, [])

  const isAdmin = member?.role === 'ROLE_ADMIN'

  return (
    <AuthContext.Provider value={{ member, login, logout, isAdmin }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)

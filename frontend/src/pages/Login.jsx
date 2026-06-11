import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(form.username, form.password)
      navigate('/')
    } catch {
      setError('아이디 또는 비밀번호가 올바르지 않습니다.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-layout" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', minHeight: '100vh' }}>
      {/* Left panel — dark */}
      <div className="auth-layout-left" style={{
        background: '#0a0a0a',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        padding: '48px',
      }}>
        <Link to="/" style={{ fontSize: '16px', fontWeight: '800', letterSpacing: '4px', color: '#fff' }}>LOV3</Link>
        <div>
          <p style={{ fontSize: '11px', letterSpacing: '3px', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '24px' }}>
            Welcome Back
          </p>
          <h1 style={{ fontSize: 'clamp(36px, 4vw, 56px)', fontWeight: '800', color: '#fff', letterSpacing: '-1px', lineHeight: 1.1 }}>
            다시 만나서<br />반가워요
          </h1>
        </div>
        <p style={{ fontSize: '12px', color: 'rgba(255,255,255,0.2)', letterSpacing: '0.5px' }}>
          © 2026 LOV3
        </p>
      </div>

      {/* Right panel — form */}
      <div className="auth-layout-right" style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '80px 80px',
        background: '#fff',
      }}>
        <div style={{ width: '100%', maxWidth: '360px' }}>
          <h2 style={{ fontSize: '26px', fontWeight: '700', letterSpacing: '-0.5px', marginBottom: '8px' }}>로그인</h2>
          <p style={{ fontSize: '14px', color: '#a0a0a0', marginBottom: '48px' }}>
            계정이 없으신가요?{' '}
            <Link to="/register" style={{ color: '#0a0a0a', fontWeight: '600' }}>회원가입</Link>
          </p>

          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
            <div className="form-group">
              <label>아이디</label>
              <input
                type="text"
                placeholder="아이디 입력"
                value={form.username}
                onChange={e => setForm(p => ({ ...p, username: e.target.value }))}
                required
              />
            </div>
            <div className="form-group">
              <label>비밀번호</label>
              <input
                type="password"
                placeholder="비밀번호 입력"
                value={form.password}
                onChange={e => setForm(p => ({ ...p, password: e.target.value }))}
                required
              />
            </div>

            {error && (
              <p style={{ fontSize: '13px', color: '#c8102e', letterSpacing: '0.2px' }}>{error}</p>
            )}

            <button type="submit" className="btn btn-dark" disabled={loading}
              style={{ padding: '16px', fontSize: '13px', letterSpacing: '1.5px', marginTop: '8px' }}>
              {loading ? '로그인 중...' : '로그인'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

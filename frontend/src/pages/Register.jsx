import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { signUp, checkId } from '../api/axios'

export default function Register() {
  const [form, setForm] = useState({ id: '', pwd: '', name: '', email: '' })
  const [idStatus, setIdStatus] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleCheckId = async () => {
    if (!form.id) return
    try {
      const res = await checkId(form.id)
      setIdStatus(res.data ? 'ok' : 'dup')
    } catch {
      setIdStatus('dup')
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (idStatus !== 'ok') { setError('아이디 중복 확인을 해주세요.'); return }
    setError('')
    setLoading(true)
    try {
      await signUp(form)
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message || '회원가입에 실패했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const set = (key) => (e) => {
    setForm(p => ({ ...p, [key]: e.target.value }))
    if (key === 'id') setIdStatus(null)
  }

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', minHeight: '100vh' }}>
      {/* Left */}
      <div style={{
        background: '#0a0a0a',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        padding: '48px',
      }}>
        <Link to="/" style={{ fontSize: '16px', fontWeight: '800', letterSpacing: '4px', color: '#fff' }}>LOV3</Link>
        <div>
          <p style={{ fontSize: '11px', letterSpacing: '3px', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '24px' }}>
            Get Started
          </p>
          <h1 style={{ fontSize: 'clamp(36px, 4vw, 56px)', fontWeight: '800', color: '#fff', letterSpacing: '-1px', lineHeight: 1.1 }}>
            새로운 만남을<br />시작하세요
          </h1>
        </div>
        <p style={{ fontSize: '12px', color: 'rgba(255,255,255,0.2)' }}>© 2026 LOV3</p>
      </div>

      {/* Right */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '80px',
        background: '#fff',
        overflowY: 'auto',
      }}>
        <div style={{ width: '100%', maxWidth: '360px' }}>
          <h2 style={{ fontSize: '26px', fontWeight: '700', letterSpacing: '-0.5px', marginBottom: '8px' }}>회원가입</h2>
          <p style={{ fontSize: '14px', color: '#a0a0a0', marginBottom: '48px' }}>
            이미 계정이 있으신가요?{' '}
            <Link to="/login" style={{ color: '#0a0a0a', fontWeight: '600' }}>로그인</Link>
          </p>

          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
            <div className="form-group">
              <label>아이디</label>
              <div style={{ display: 'flex', alignItems: 'flex-end', gap: '12px' }}>
                <input
                  type="text"
                  placeholder="아이디"
                  value={form.id}
                  onChange={set('id')}
                  required
                  style={{ flex: 1,
                    borderBottomColor: idStatus === 'ok' ? '#155724' : idStatus === 'dup' ? '#c8102e' : undefined
                  }}
                />
                <button type="button" onClick={handleCheckId}
                  style={{
                    background: 'none', border: 'none', padding: '14px 0',
                    fontSize: '12px', fontWeight: '700', letterSpacing: '0.5px',
                    color: '#0a0a0a', borderBottom: '1.5px solid #0a0a0a',
                    cursor: 'pointer', flexShrink: 0,
                  }}>
                  중복확인
                </button>
              </div>
              {idStatus === 'ok'  && <span style={{ fontSize: '12px', color: '#155724' }}>사용 가능한 아이디입니다</span>}
              {idStatus === 'dup' && <span style={{ fontSize: '12px', color: '#c8102e' }}>이미 사용 중인 아이디입니다</span>}
            </div>

            <div className="form-group">
              <label>비밀번호</label>
              <input type="password" placeholder="비밀번호" value={form.pwd} onChange={set('pwd')} required />
            </div>

            <div className="form-group">
              <label>이름</label>
              <input type="text" placeholder="이름" value={form.name} onChange={set('name')} required />
            </div>

            <div className="form-group">
              <label>이메일</label>
              <input type="email" placeholder="이메일 (선택)" value={form.email} onChange={set('email')} />
            </div>

            {error && <p style={{ fontSize: '13px', color: '#c8102e' }}>{error}</p>}

            <button type="submit" className="btn btn-dark" disabled={loading}
              style={{ padding: '16px', fontSize: '13px', letterSpacing: '1.5px', marginTop: '8px' }}>
              {loading ? '가입 중...' : '가입하기'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

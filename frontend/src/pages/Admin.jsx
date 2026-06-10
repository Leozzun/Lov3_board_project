import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { adminGetMembers, adminDeleteMember } from '../api/axios'
import { useAuth } from '../contexts/AuthContext'

export default function Admin() {
  const { isAdmin, member: me } = useAuth()
  const navigate = useNavigate()
  const [allMembers, setAllMembers] = useState([])   // 통계용 전체
  const [members, setMembers] = useState([])          // 테이블용 (자기 자신 제외)
  const [loading, setLoading] = useState(true)
  const [toast, setToast] = useState('')

  const showToast = (msg) => { setToast(msg); setTimeout(() => setToast(''), 3000) }

  const fetchMembers = async () => {
    setLoading(true)
    try {
      const res = await adminGetMembers()
      setAllMembers(res.data)
      setMembers(res.data.filter(m => m.memberNo !== me?.memberNo))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (!isAdmin) { navigate('/'); return }
    fetchMembers()
  }, [isAdmin])

  const handleDelete = async (memberNo, name) => {
    if (!window.confirm(`${name}님을 강제 탈퇴시킬까요?`)) return
    try {
      await adminDeleteMember(memberNo)
      showToast(`${name}님을 탈퇴 처리했습니다`)
      fetchMembers()
    } catch {
      showToast('삭제에 실패했습니다')
    }
  }

  const roleLabel = (role) => role === 'ROLE_ADMIN' ? 'ADMIN' : 'USER'

  return (
    <div className="page">
      {/* Header */}
      <div style={{ background: '#0a0a0a', padding: '100px 0 80px' }}>
        <div className="container">
          <p style={{
            fontSize: '11px', fontWeight: '700', letterSpacing: '4px',
            color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '20px',
          }}>
            Admin
          </p>
          <h1 style={{
            fontSize: 'clamp(36px, 5vw, 64px)', fontWeight: '800',
            color: '#fff', letterSpacing: '-1.5px', lineHeight: 1.05,
          }}>
            회원 관리
          </h1>
        </div>
      </div>

      <div className="container" style={{ padding: '80px 40px' }}>
        {/* 통계 */}
        <div style={{ display: 'flex', gap: '2px', marginBottom: '64px' }}>
          {[
            { label: '전체 회원', value: allMembers.length },
            { label: '일반 회원', value: allMembers.filter(m => m.role !== 'ROLE_ADMIN').length },
            { label: '관리자', value: allMembers.filter(m => m.role === 'ROLE_ADMIN').length },
          ].map(({ label, value }) => (
            <div key={label} style={{ background: '#f5f5f5', padding: '28px 36px', flex: 1 }}>
              <p style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '2px', color: '#a0a0a0', textTransform: 'uppercase', marginBottom: '12px' }}>
                {label}
              </p>
              <p style={{ fontSize: '36px', fontWeight: '800', letterSpacing: '-1px', color: '#0a0a0a' }}>
                {loading ? '—' : value}
              </p>
            </div>
          ))}
        </div>

        {/* 회원 목록 */}
        {loading ? (
          <div className="loading">불러오는 중</div>
        ) : members.length === 0 ? (
          <div className="empty"><div className="empty-icon">👤</div><p>회원이 없어요</p></div>
        ) : (
          <div>
            {/* 테이블 헤더 */}
            <div style={{
              display: 'grid',
              gridTemplateColumns: '60px 1fr 1fr 120px 80px 120px',
              padding: '12px 0',
              borderBottom: '2px solid #0a0a0a',
              fontSize: '11px', fontWeight: '700', letterSpacing: '1.5px',
              textTransform: 'uppercase', color: '#a0a0a0',
            }}>
              <span>No</span>
              <span>이름 / 아이디</span>
              <span>이메일</span>
              <span>가입일</span>
              <span>권한</span>
              <span style={{ textAlign: 'right' }}>액션</span>
            </div>

            {members.map((m, i) => (
              <div key={m.memberNo}>
                <div style={{
                  display: 'grid',
                  gridTemplateColumns: '60px 1fr 1fr 120px 80px 120px',
                  alignItems: 'center',
                  padding: '24px 0',
                  gap: '16px',
                }}>
                  <span style={{ fontSize: '13px', color: '#c0c0c0', fontWeight: '600' }}>
                    {String(i + 1).padStart(2, '0')}
                  </span>
                  <div>
                    <p style={{ fontSize: '15px', fontWeight: '600', color: '#0a0a0a', marginBottom: '3px' }}>
                      {m.name}
                    </p>
                    <p style={{ fontSize: '12px', color: '#a0a0a0' }}>@{m.id}</p>
                  </div>
                  <span style={{ fontSize: '13px', color: '#5a5a5a' }}>{m.email || '—'}</span>
                  <span style={{ fontSize: '12px', color: '#a0a0a0' }}>
                    {m.regDate?.slice(0, 10) || '—'}
                  </span>
                  <span>
                    <span style={{
                      fontSize: '10px', fontWeight: '700', letterSpacing: '1.5px',
                      padding: '3px 8px',
                      background: m.role === 'ROLE_ADMIN' ? '#0a0a0a' : '#f0f0f0',
                      color: m.role === 'ROLE_ADMIN' ? '#fff' : '#5a5a5a',
                    }}>
                      {roleLabel(m.role)}
                    </span>
                  </span>
                  <div style={{ textAlign: 'right' }}>
                    {m.role !== 'ROLE_ADMIN' && (
                      <button
                        onClick={() => handleDelete(m.memberNo, m.name)}
                        className="btn btn-outline-dark"
                        style={{ fontSize: '11px', padding: '6px 14px', color: '#c8102e', borderColor: '#c8102e' }}
                      >
                        강제탈퇴
                      </button>
                    )}
                  </div>
                </div>
                {i < members.length - 1 && <div className="divider" />}
              </div>
            ))}
          </div>
        )}
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

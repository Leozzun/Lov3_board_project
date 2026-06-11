import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { useState, useEffect } from 'react'

export default function Navbar() {
  const { member, logout, isAdmin } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [scrolled, setScrolled] = useState(false)
  const [menuOpen, setMenuOpen] = useState(false)

  const isHome = location.pathname === '/'

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 60)
    window.addEventListener('scroll', onScroll)
    return () => window.removeEventListener('scroll', onScroll)
  }, [])

  useEffect(() => { setMenuOpen(false) }, [location.pathname])

  const handleLogout = () => { logout(); navigate('/'); setMenuOpen(false) }

  const transparent = isHome && !scrolled
  const textColor = transparent ? 'rgba(255,255,255,0.85)' : '#0a0a0a'

  const linkStyle = {
    fontSize: '13px', fontWeight: '600', letterSpacing: '1px',
    textTransform: 'uppercase', color: textColor, opacity: 0.7,
    transition: 'opacity 0.2s',
  }

  return (
    <>
      <nav style={{
        position: 'fixed',
        top: 0, left: 0, right: 0,
        zIndex: 100,
        height: '72px',
        display: 'flex',
        alignItems: 'center',
        background: transparent ? 'transparent' : 'rgba(255,255,255,0.97)',
        borderBottom: transparent ? 'none' : '1px solid #e8e8e8',
        backdropFilter: transparent ? 'none' : 'blur(12px)',
        transition: 'all 0.4s cubic-bezier(0.4,0,0.2,1)',
      }}>
        <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', width: '100%' }}>

          <Link to="/" style={{
            fontSize: '20px', fontWeight: '800', letterSpacing: '4px',
            textTransform: 'uppercase', color: textColor, transition: 'color 0.4s',
          }}>
            LOV3
          </Link>

          {/* 데스크탑 메뉴 */}
          <div className="nav-desktop" style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
            {member ? (
              <>
                <Link to="/boards" style={linkStyle}
                  onMouseEnter={e => e.target.style.opacity = '1'}
                  onMouseLeave={e => e.target.style.opacity = '0.7'}>게시판</Link>
                <Link to="/places" style={linkStyle}
                  onMouseEnter={e => e.target.style.opacity = '1'}
                  onMouseLeave={e => e.target.style.opacity = '0.7'}>장소</Link>
                <Link to="/mypage" style={linkStyle}
                  onMouseEnter={e => e.target.style.opacity = '1'}
                  onMouseLeave={e => e.target.style.opacity = '0.7'}>마이페이지</Link>
                {isAdmin && (
                  <Link to="/admin" style={linkStyle}
                    onMouseEnter={e => e.target.style.opacity = '1'}
                    onMouseLeave={e => e.target.style.opacity = '0.7'}>관리자</Link>
                )}
                <button onClick={handleLogout}
                  className={transparent ? 'btn btn-outline-white' : 'btn btn-dark'}
                  style={{ padding: '10px 22px', fontSize: '12px' }}>
                  로그아웃
                </button>
              </>
            ) : (
              <>
                <Link to="/login" style={{ ...linkStyle, opacity: 0.75 }}>로그인</Link>
                <Link to="/register"
                  className={transparent ? 'btn btn-outline-white' : 'btn btn-dark'}
                  style={{ padding: '10px 22px', fontSize: '12px' }}>
                  회원가입
                </Link>
              </>
            )}
          </div>

          {/* 햄버거 버튼 (모바일) */}
          <button
            className="nav-hamburger"
            onClick={() => setMenuOpen(!menuOpen)}
            style={{
              display: 'none',
              flexDirection: 'column', justifyContent: 'center', gap: '5px',
              background: 'none', border: 'none', cursor: 'pointer', padding: '8px',
            }}
          >
            <span style={{
              display: 'block', width: '22px', height: '2px',
              background: textColor,
              transform: menuOpen ? 'translateY(7px) rotate(45deg)' : 'none',
              transition: 'all 0.3s',
            }} />
            <span style={{
              display: 'block', width: '22px', height: '2px',
              background: textColor,
              opacity: menuOpen ? 0 : 1,
              transition: 'all 0.3s',
            }} />
            <span style={{
              display: 'block', width: '22px', height: '2px',
              background: textColor,
              transform: menuOpen ? 'translateY(-7px) rotate(-45deg)' : 'none',
              transition: 'all 0.3s',
            }} />
          </button>
        </div>
      </nav>

      {/* 모바일 풀스크린 메뉴 */}
      {menuOpen && (
        <div style={{
          position: 'fixed',
          top: '72px', left: 0, right: 0, bottom: 0,
          background: '#0a0a0a',
          zIndex: 99,
          display: 'flex',
          flexDirection: 'column',
          padding: '48px 32px',
          overflowY: 'auto',
        }}>
          {member ? (
            <>
              {[
                { to: '/boards', label: '게시판' },
                { to: '/places', label: '장소' },
                { to: '/mypage', label: '마이페이지' },
                ...(isAdmin ? [{ to: '/admin', label: '관리자' }] : []),
              ].map(({ to, label }) => (
                <Link key={to} to={to} style={{
                  fontSize: '32px', fontWeight: '800', color: '#fff',
                  letterSpacing: '-0.5px', padding: '16px 0',
                  borderBottom: '1px solid rgba(255,255,255,0.08)',
                  opacity: 0.85,
                }}>
                  {label}
                </Link>
              ))}
              <button onClick={handleLogout} style={{
                marginTop: '40px', background: 'none', border: '1.5px solid rgba(255,255,255,0.3)',
                color: 'rgba(255,255,255,0.7)', padding: '16px', fontSize: '13px',
                fontWeight: '600', letterSpacing: '1px', textTransform: 'uppercase',
                cursor: 'pointer', alignSelf: 'flex-start', minWidth: '120px',
              }}>
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link to="/login" style={{
                fontSize: '32px', fontWeight: '800', color: '#fff',
                letterSpacing: '-0.5px', padding: '16px 0',
                borderBottom: '1px solid rgba(255,255,255,0.08)', opacity: 0.85,
              }}>로그인</Link>
              <Link to="/register" style={{
                fontSize: '32px', fontWeight: '800', color: '#fff',
                letterSpacing: '-0.5px', padding: '16px 0',
                borderBottom: '1px solid rgba(255,255,255,0.08)', opacity: 0.85,
              }}>회원가입</Link>
            </>
          )}
        </div>
      )}
    </>
  )
}

import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { getPlaces, getBoards, getReceivedRequests } from '../api/axios'
import { useAuth } from '../contexts/AuthContext'
import picnicBg from '../assets/피크닉.jpg'

export default function Home() {
  const { member } = useAuth()
  return member ? <HomeLoggedIn member={member} /> : <HomeGuest />
}

/* ─────────────────────────────────────────────
   비로그인 홈
───────────────────────────────────────────── */
function HomeGuest() {
  const [places, setPlaces] = useState([])
  const [boards, setBoards] = useState([])
  const navigate = useNavigate()
  const picnicBgYPosition = '80%';

  useEffect(() => {
    getPlaces().then(r => setPlaces(r.data.slice(0, 6))).catch(() => {})
    getBoards(0, 3).then(r => setBoards(r.data.content)).catch(() => {})
  }, [])

  return (
    <div>
      {/* ── HERO ── 실제 야경 사진 배경 */}
      <section style={{
        height: '100vh',
        minHeight: '700px',
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        overflow: 'hidden',
      }}>
        {/* 배경 이미지 */}
        <div style={{
          position: 'absolute', inset: 0,
          backgroundImage: 'url(https://images.unsplash.com/photo-1546874177-9e664107314e?w=1920&q=80)',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          filter: 'brightness(0.65)',
        }} />

        {/* 오버레이 */}
        <div style={{
          position: 'absolute', inset: 0,
          background: 'linear-gradient(to bottom, rgba(0,0,0,0.15) 0%, rgba(0,0,0,0.4) 100%)',
        }} />

        {/* 콘텐츠 */}
        <div style={{ position: 'relative', padding: '0 24px', maxWidth: '820px' }}>
          <p style={{
            fontSize: '11px', fontWeight: '700', letterSpacing: '5px',
            textTransform: 'uppercase', color: 'rgba(255,255,255,0.5)',
            marginBottom: '28px',
          }}>
            Date Matching Platform
          </p>
          <h1 style={{
            fontSize: 'clamp(44px, 7vw, 88px)',
            fontWeight: '800', color: '#ffffff',
            lineHeight: 1.05, letterSpacing: '-2px',
            marginBottom: '28px',
          }}>
            특별한 장소에서<br />
            <span style={{ color: 'rgba(255,255,255,0.5)' }}>특별한 사람과</span>
          </h1>
          <p style={{
            fontSize: '17px', color: 'rgba(255,255,255,0.55)',
            lineHeight: 1.9, marginBottom: '52px', maxWidth: '400px', margin: '0 auto 52px',
          }}>
            마음에 드는 장소를 선택하고<br />같은 취향의 사람에게 데이트를 신청하세요
          </p>
          <div style={{ display: 'flex', gap: '14px', justifyContent: 'center', flexWrap: 'wrap' }}>
            <Link to="/register" className="btn btn-white" style={{ padding: '16px 40px', fontSize: '13px' }}>
              무료로 시작하기
            </Link>
            <Link to="/boards" className="btn btn-outline-white" style={{ padding: '16px 40px', fontSize: '13px' }}>
              게시판 둘러보기
            </Link>
          </div>
        </div>

        {/* 스크롤 */}
        <div style={{
          position: 'absolute', bottom: '40px', left: '50%',
          transform: 'translateX(-50%)',
          display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px',
          color: 'rgba(255,255,255,0.25)', fontSize: '10px', letterSpacing: '3px',
        }}>
          <div style={{ width: '1px', height: '48px', background: 'linear-gradient(to bottom, transparent, rgba(255,255,255,0.25))' }} />
          SCROLL
        </div>
      </section>

      {/* ── INTRO ── */}
      <section className="section" style={{ background: '#fff' }}>
        <div className="container">
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '100px', alignItems: 'center' }}>
            <div>
              <p className="section-label">About LOV3</p>
              <h2 className="section-title">장소가 연결하는<br />새로운 만남</h2>
              <p className="section-subtitle" style={{ marginBottom: '40px' }}>
                맛집, 공원, 카페 — 당신이 좋아하는 장소를 공유하고,
                같은 곳을 좋아하는 사람과 데이트를 시작하세요.
              </p>
              <Link to="/register" className="btn btn-dark" style={{ fontSize: '12px' }}>
                회원가입하기
              </Link>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2px' }}>
              {[
                { bg: '#0a0a0a', color: '#fff', icon: '📍', label: '장소 기반 매칭' },
                { bg: '#f0f0f0', color: '#0a0a0a', icon: '💌', label: '직접 데이트 신청' },
                { bg: '#f0f0f0', color: '#0a0a0a', icon: '🔒', label: '안전한 매칭' },
                { bg: '#0a0a0a', color: '#fff', icon: '✓', label: '무료 서비스' },
              ].map((item, i) => (
                <div key={i} style={{ background: item.bg, color: item.color, padding: '40px 28px' }}>
                  <div style={{ fontSize: '28px', marginBottom: '16px' }}>{item.icon}</div>
                  <div style={{ fontSize: '14px', fontWeight: '600', letterSpacing: '0.3px' }}>{item.label}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      <div className="divider" />

      {/* ── PLACES ── */}
      <section className="section">
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '56px' }}>
            <div>
              <p className="section-label">Destinations</p>
              <h2 className="section-title">데이트 장소</h2>
            </div>
            <Link to="/places" style={{ fontSize: '13px', fontWeight: '700', letterSpacing: '1px', textTransform: 'uppercase', color: '#0a0a0a' }}>
              전체 보기 →
            </Link>
          </div>
          {places.length === 0 ? (
            <div className="empty"><div className="empty-icon">📍</div><p>등록된 장소가 없어요</p></div>
          ) : (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '2px' }}>
              {places.slice(0, 3).map((place, i) => (
                <PlaceCell key={place.placeId} place={place} index={i} />
              ))}
            </div>
          )}
        </div>
      </section>

      <div className="divider" />

      {/* ── BOARDS ── */}
      <section className="section" style={{ background: '#f5f5f5' }}>
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '56px' }}>
            <div>
              <p className="section-label">Community</p>
              <h2 className="section-title">최신 신청글</h2>
            </div>
            <Link to="/boards" style={{ fontSize: '13px', fontWeight: '700', letterSpacing: '1px', textTransform: 'uppercase', color: '#0a0a0a' }}>
              전체 보기 →
            </Link>
          </div>
          {boards.length === 0 ? (
            <div className="empty"><div className="empty-icon">💌</div><p>게시글이 없어요</p></div>
          ) : (
            <div>
              {boards.map((board, i) => (
                <div key={board.boardId}>
                  <Link to={`/boards/${board.boardId}`} style={{
                    display: 'grid', gridTemplateColumns: '60px 1fr auto',
                    alignItems: 'center', gap: '40px', padding: '36px 0', transition: 'opacity 0.2s',
                  }}
                    onMouseEnter={e => e.currentTarget.style.opacity = '0.5'}
                    onMouseLeave={e => e.currentTarget.style.opacity = '1'}
                  >
                    <span style={{ fontSize: '40px', fontWeight: '800', color: '#d8d8d8', letterSpacing: '-2px', lineHeight: 1 }}>
                      {String(i + 1).padStart(2, '0')}
                    </span>
                    <div>
                      <p style={{ fontSize: '11px', letterSpacing: '2px', color: '#a0a0a0', marginBottom: '8px', textTransform: 'uppercase' }}>
                        {board.place?.placeName || '장소'}
                      </p>
                      <h3 style={{ fontSize: '19px', fontWeight: '700', letterSpacing: '-0.3px', marginBottom: '6px' }}>{board.title}</h3>
                      <p style={{ fontSize: '14px', color: '#5a5a5a' }}>
                        {board.content?.slice(0, 80)}{board.content?.length > 80 ? '...' : ''}
                      </p>
                    </div>
                    <span style={{ fontSize: '20px', opacity: 0.25 }}>→</span>
                  </Link>
                  {i < boards.length - 1 && <div className="divider" />}
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* ── CTA ── */}
      <section style={{
        position: 'relative', overflow: 'hidden',
        padding: '160px 40px', textAlign: 'center',
      }}>
        <div style={{
          position: 'absolute', inset: 0,
          backgroundImage: `url(${picnicBg})`,
          backgroundSize: 'cover', backgroundPosition: `center ${picnicBgYPosition}`,
          backgroundRepeat: 'no-repeat',
          filter: 'brightness(0.3)',
        }} />
        <div style={{ position: 'relative' }}>
          <p style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '4px', color: 'rgba(255,255,255,0.4)', marginBottom: '28px', textTransform: 'uppercase' }}>
            Join LOV3
          </p>
          <h2 style={{ fontSize: 'clamp(36px, 5vw, 64px)', fontWeight: '800', color: '#fff', letterSpacing: '-1.5px', lineHeight: 1.1, marginBottom: '48px' }}>
            지금 시작해보세요
          </h2>
          <Link to="/register" className="btn btn-white" style={{ padding: '18px 48px', fontSize: '13px' }}>
            무료 회원가입
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer style={{ padding: '40px', background: '#0a0a0a', borderTop: '1px solid #1a1a1a', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
        <span style={{ fontSize: '14px', fontWeight: '800', letterSpacing: '4px', color: 'rgba(255,255,255,0.25)' }}>LOV3</span>
        <span style={{ fontSize: '12px', color: 'rgba(255,255,255,0.2)' }}>© 2026 LOV3. All rights reserved.</span>
      </footer>
    </div>
  )
}

/* ─────────────────────────────────────────────
   로그인 후 홈
───────────────────────────────────────────── */
function HomeLoggedIn({ member }) {
  const [received, setReceived] = useState([])
  const [boards, setBoards] = useState([])
  const [places, setPlaces] = useState([])
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    Promise.all([
      getReceivedRequests().catch(() => ({ data: [] })),
      getBoards(0, 5).catch(() => ({ data: { content: [] } })),
      getPlaces().catch(() => ({ data: [] })),
    ]).then(([r, b, p]) => {
      setReceived(r.data)
      setBoards(b.data.content)
      setPlaces(p.data.slice(0, 4))
    }).finally(() => setLoading(false))
  }, [])

  const pending = received.filter(r => r.status === 'PENDING')

  return (
    <div style={{ background: '#fafafa', minHeight: '100vh' }}>
      {/* Welcome header */}
      <div style={{ background: '#0a0a0a', padding: '100px 0 72px' }}>
        <div className="container">
          <p style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '4px', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '16px' }}>
            Welcome Back
          </p>
          <h1 style={{ fontSize: 'clamp(32px, 5vw, 56px)', fontWeight: '800', color: '#fff', letterSpacing: '-1px', lineHeight: 1.05, marginBottom: '32px' }}>
            안녕하세요, {member.name}님
          </h1>
          <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
            <button onClick={() => navigate('/boards')} className="btn btn-white" style={{ fontSize: '12px' }}>
              게시판 보기
            </button>
            <button onClick={() => navigate('/places')} className="btn btn-outline-white" style={{ fontSize: '12px' }}>
              장소 탐색
            </button>
            <button onClick={() => navigate('/mypage')} className="btn btn-outline-white" style={{ fontSize: '12px' }}>
              내 신청 현황
            </button>
          </div>
        </div>
      </div>

      <div className="container" style={{ padding: '64px 40px' }}>
        {/* 알림 — 대기중 신청 */}
        {pending.length > 0 && (
          <div onClick={() => navigate('/mypage')} style={{
            background: '#0a0a0a', color: '#fff',
            padding: '20px 28px', marginBottom: '48px',
            display: 'flex', alignItems: 'center', justifyContent: 'space-between',
            cursor: 'pointer', transition: 'opacity 0.2s',
          }}
            onMouseEnter={e => e.currentTarget.style.opacity = '0.85'}
            onMouseLeave={e => e.currentTarget.style.opacity = '1'}
          >
            <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
              <span style={{
                background: '#fff', color: '#0a0a0a',
                fontWeight: '800', fontSize: '13px',
                padding: '4px 10px', borderRadius: '2px',
              }}>{pending.length}</span>
              <span style={{ fontSize: '15px', fontWeight: '500' }}>
                읽지 않은 데이트 신청이 있어요
              </span>
            </div>
            <span style={{ opacity: 0.5 }}>→</span>
          </div>
        )}

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '48px', alignItems: 'start' }}>
          {/* Left — 최신 게시글 */}
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
              <h2 style={{ fontSize: '18px', fontWeight: '700', letterSpacing: '-0.3px' }}>최신 게시글</h2>
              <Link to="/boards" style={{ fontSize: '12px', fontWeight: '700', letterSpacing: '1px', textTransform: 'uppercase', color: '#a0a0a0' }}>
                전체 →
              </Link>
            </div>
            <div style={{ background: '#fff', borderTop: '2px solid #0a0a0a' }}>
              {loading ? (
                <div style={{ padding: '40px', textAlign: 'center', color: '#c0c0c0', fontSize: '13px' }}>불러오는 중</div>
              ) : boards.length === 0 ? (
                <div style={{ padding: '40px', textAlign: 'center', color: '#c0c0c0', fontSize: '13px' }}>게시글이 없어요</div>
              ) : boards.map((board, i) => (
                <div key={board.boardId}>
                  <Link to={`/boards/${board.boardId}`} style={{
                    display: 'block', padding: '20px 24px', transition: 'background 0.2s',
                  }}
                    onMouseEnter={e => e.currentTarget.style.background = '#f8f8f8'}
                    onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
                  >
                    <p style={{ fontSize: '11px', letterSpacing: '1px', color: '#a0a0a0', marginBottom: '6px', textTransform: 'uppercase' }}>
                      {board.place?.placeName || '장소'}
                    </p>
                    <p style={{ fontSize: '15px', fontWeight: '600', letterSpacing: '-0.2px', marginBottom: '4px' }}>{board.title}</p>
                    <p style={{ fontSize: '13px', color: '#a0a0a0' }}>{board.member?.name}</p>
                  </Link>
                  {i < boards.length - 1 && <div className="divider" />}
                </div>
              ))}
            </div>
          </div>

          {/* Right — 장소 + 빠른 글쓰기 */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
            {/* 빠른 글쓰기 */}
            <div style={{ background: '#fff', borderTop: '2px solid #0a0a0a', padding: '28px' }}>
              <h3 style={{ fontSize: '15px', fontWeight: '700', marginBottom: '16px' }}>데이트 신청글 쓰기</h3>
              <p style={{ fontSize: '13px', color: '#a0a0a0', marginBottom: '20px', lineHeight: 1.6 }}>
                원하는 장소를 선택하고<br />함께할 사람을 찾아보세요
              </p>
              <button onClick={() => navigate('/boards')} className="btn btn-dark" style={{ width: '100%', fontSize: '12px' }}>
                글 쓰러 가기
              </button>
            </div>

            {/* 장소 빠른 접근 */}
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                <h3 style={{ fontSize: '15px', fontWeight: '700' }}>장소</h3>
                <Link to="/places" style={{ fontSize: '12px', fontWeight: '700', letterSpacing: '1px', textTransform: 'uppercase', color: '#a0a0a0' }}>
                  전체 →
                </Link>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2px' }}>
                {loading ? null : places.map((place, i) => (
                  <div key={place.placeId}
                    onClick={() => navigate(`/boards?placeId=${place.placeId}`)}
                    style={{
                      height: '120px', cursor: 'pointer', position: 'relative', overflow: 'hidden',
                      background: place.placeImg ? `url(${place.placeImg}) center/cover` : ['#1a1a1a','#2a2a2a','#222','#181818'][i%4],
                      transition: 'opacity 0.2s',
                    }}
                    onMouseEnter={e => e.currentTarget.style.opacity = '0.8'}
                    onMouseLeave={e => e.currentTarget.style.opacity = '1'}
                  >
                    <div style={{ position: 'absolute', inset: 0, background: 'rgba(0,0,0,0.45)', display: 'flex', alignItems: 'flex-end', padding: '12px' }}>
                      <p style={{ fontSize: '13px', fontWeight: '700', color: '#fff', letterSpacing: '-0.2px' }}>{place.placeName}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function PlaceCell({ place, index }) {
  const navigate = useNavigate()
  const darks = ['#111', '#1a1a1a', '#141414']
  return (
    <div onClick={() => navigate(`/boards?placeId=${place.placeId}`)} style={{
      position: 'relative', height: '400px', cursor: 'pointer', overflow: 'hidden',
      background: place.placeImg ? `url(${place.placeImg}) center/cover` : darks[index % 3],
    }}>
      <div style={{
        position: 'absolute', inset: 0,
        background: 'linear-gradient(to top, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.05) 60%)',
        transition: 'background 0.3s',
      }} />
      <div style={{ position: 'absolute', bottom: 0, left: 0, padding: '32px 28px', color: '#fff' }}>
        <p style={{ fontSize: '11px', letterSpacing: '2px', opacity: 0.5, marginBottom: '8px', textTransform: 'uppercase' }}>장소</p>
        <h3 style={{ fontSize: '20px', fontWeight: '700', letterSpacing: '-0.3px' }}>{place.placeName}</h3>
      </div>
    </div>
  )
}

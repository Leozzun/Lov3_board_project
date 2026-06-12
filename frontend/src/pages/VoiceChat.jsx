import { useState, useRef, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Room, RoomEvent } from 'livekit-client'
import api from '../api/axios'

const ROOM_NAME = 'lov3-voice-room'

export default function VoiceChat() {
  const { isAdmin, member } = useAuth()
  const navigate = useNavigate()
  const [status, setStatus] = useState('대기 중')
  const [connected, setConnected] = useState(false)
  const [participants, setParticipants] = useState([])
  const [micEnabled, setMicEnabled] = useState(true)
  const [toast, setToast] = useState('')
  const roomRef = useRef(null)
  const audioElsRef = useRef([])

  const showToast = (msg) => { setToast(msg); setTimeout(() => setToast(''), 3000) }

  useEffect(() => {
    if (!isAdmin) { navigate('/'); return }
    return () => {
      roomRef.current?.disconnect()
      audioElsRef.current.forEach(el => el.remove())
    }
  }, [])

  const syncParticipants = (room) => {
    const remote = [...room.remoteParticipants.values()].map(p => ({
      identity: p.identity,
      isLocal: false,
    }))
    setParticipants([{ identity: room.localParticipant.identity, isLocal: true }, ...remote])
  }

  const joinRoom = async () => {
    try {
      const res = await api.get(`/voice/token?roomName=${ROOM_NAME}&userName=${encodeURIComponent(member.name)}`)
      const { token, url } = res.data

      const room = new Room()
      roomRef.current = room

      room.on(RoomEvent.ParticipantConnected, () => syncParticipants(room))
      room.on(RoomEvent.ParticipantDisconnected, () => syncParticipants(room))
      room.on(RoomEvent.TrackSubscribed, (track) => {
        if (track.kind === 'audio') {
          const el = track.attach()
          document.body.appendChild(el)
          audioElsRef.current.push(el)
        }
      })
      room.on(RoomEvent.TrackUnsubscribed, (track) => track.detach())
      room.on(RoomEvent.Disconnected, () => {
        setConnected(false)
        setParticipants([])
        setStatus('대기 중')
      })

      await room.connect(url, token)
      await room.localParticipant.setMicrophoneEnabled(true)

      syncParticipants(room)
      setConnected(true)
      setStatus(`${member.name} 입장 완료!`)
      showToast('음성 채팅에 입장했습니다')
    } catch (e) {
      console.error(e)
      showToast('입장에 실패했습니다')
    }
  }

  const leaveRoom = async () => {
    await roomRef.current?.disconnect()
    roomRef.current = null
    audioElsRef.current.forEach(el => el.remove())
    audioElsRef.current = []
    setConnected(false)
    setParticipants([])
    setStatus('대기 중')
    showToast('음성 채팅에서 나갔습니다')
  }

  const toggleMic = async () => {
    if (!roomRef.current) return
    const next = !micEnabled
    await roomRef.current.localParticipant.setMicrophoneEnabled(next)
    setMicEnabled(next)
  }

  return (
    <div className="page">
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
            음성 채팅
          </h1>
        </div>
      </div>

      <div className="container" style={{ padding: '80px 40px' }}>
        {/* 상태 카드 */}
        <div style={{ display: 'flex', gap: '2px', marginBottom: '64px' }}>
          {[
            { label: '방 이름', value: ROOM_NAME },
            { label: '참여 인원', value: `${participants.length}명` },
            { label: '상태', value: connected ? '연결됨' : '대기 중' },
          ].map(({ label, value }) => (
            <div key={label} style={{ background: '#f5f5f5', padding: '28px 36px', flex: 1 }}>
              <p style={{
                fontSize: '11px', fontWeight: '700', letterSpacing: '2px',
                color: '#a0a0a0', textTransform: 'uppercase', marginBottom: '12px',
              }}>
                {label}
              </p>
              <p style={{ fontSize: '24px', fontWeight: '800', letterSpacing: '-0.5px', color: '#0a0a0a' }}>
                {value}
              </p>
            </div>
          ))}
        </div>

        {/* 컨트롤 버튼 */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '48px' }}>
          {!connected ? (
            <button onClick={joinRoom} className="btn btn-dark" style={{ padding: '14px 32px', fontSize: '13px' }}>
              🎤 입장하기
            </button>
          ) : (
            <>
              <button
                onClick={toggleMic}
                className="btn btn-outline-dark"
                style={{ padding: '14px 32px', fontSize: '13px' }}
              >
                {micEnabled ? '🔇 마이크 끄기' : '🎤 마이크 켜기'}
              </button>
              <button
                onClick={leaveRoom}
                className="btn btn-outline-dark"
                style={{ padding: '14px 32px', fontSize: '13px', color: '#c8102e', borderColor: '#c8102e' }}
              >
                🚪 나가기
              </button>
            </>
          )}
          <span style={{ fontSize: '13px', color: '#a0a0a0', marginLeft: '8px' }}>{status}</span>
        </div>

        {/* 참여자 목록 */}
        {connected && (
          participants.length === 0 ? (
            <div className="empty">
              <div className="empty-icon">🎤</div>
              <p>아직 참여자가 없어요</p>
            </div>
          ) : (
            <div>
              <div style={{
                display: 'grid',
                gridTemplateColumns: '60px 1fr 120px',
                padding: '12px 0',
                borderBottom: '2px solid #0a0a0a',
                fontSize: '11px', fontWeight: '700', letterSpacing: '1.5px',
                textTransform: 'uppercase', color: '#a0a0a0',
              }}>
                <span>No</span>
                <span>참여자</span>
                <span>구분</span>
              </div>

              {participants.map((p, i) => (
                <div key={p.identity}>
                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: '60px 1fr 120px',
                    alignItems: 'center',
                    padding: '20px 0',
                    gap: '16px',
                  }}>
                    <span style={{ fontSize: '13px', color: '#c0c0c0', fontWeight: '600' }}>
                      {String(i + 1).padStart(2, '0')}
                    </span>
                    <span style={{ fontSize: '15px', fontWeight: '600', color: '#0a0a0a' }}>
                      {p.identity}
                    </span>
                    <span>
                      <span style={{
                        fontSize: '10px', fontWeight: '700', letterSpacing: '1.5px',
                        padding: '3px 8px',
                        background: p.isLocal ? '#0a0a0a' : '#f0f0f0',
                        color: p.isLocal ? '#fff' : '#5a5a5a',
                      }}>
                        {p.isLocal ? 'ME' : 'REMOTE'}
                      </span>
                    </span>
                  </div>
                  {i < participants.length - 1 && <div className="divider" />}
                </div>
              ))}
            </div>
          )
        )}
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

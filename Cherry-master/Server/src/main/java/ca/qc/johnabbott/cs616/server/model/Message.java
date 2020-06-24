package ca.qc.johnabbott.cs616.server.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;

    @ManyToOne
    @JoinColumn(name = "match")
    public Match match;

    @ManyToOne
    @JoinColumn(name = "fromuser")
    public User fromuser;

    @Column(name = "message")
    public String message;

    @Column(name = "timesent")
    @Temporal(TemporalType.TIMESTAMP)
    public Date timesent;

    public Match get_match() {
        return match;
    }

    public void set_match(Match _match) {
        this.match = _match;
    }
}

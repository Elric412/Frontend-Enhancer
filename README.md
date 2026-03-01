# Frontend Enhancer

**A skill that makes AI build frontends that don't look like AI built them.**

---

## The Problem

Every AI builds the same frontend. Same fonts (Inter, Roboto). Same colors (purple-blue gradient). Same layout (hero → 3 cards → CTA → footer). Same hover effects (color shift). Same flat backgrounds.

The community calls it **AI slop**. It happens because LLMs converge on the most probable output from their training data — and the most probable frontend is a generic SaaS landing page.

## What This Does

Frontend Enhancer is a skill that breaks that convergence. It installs a different design system into the model's reasoning:

- **4-layer color** — Neutrals with spatial hierarchy, accent ramps (not a single hex), semantic colors at matched perceptual brightness, and behavioral colors that respond to scroll/state. All derived from one real-world source using OKLCH.
- **Creative layouts** — Split-screens, editorial spreads, overlapping panels, full-bleed sections, broken containers. Not stacked centered boxes. Every section structurally unique.
- **Motion choreography** — Staggered load sequences, scroll-triggered reveals, state morphing with physical weight. Bound to a motion map so it actually gets implemented. Every interactive element responds with character.
- **Typography as design material** — Intentional font geometry, smart pairing across contrast axes, poster-grade hero composition. Not just readable content but visual architecture.
- **Custom SVG** — Geometric patterns, abstract shapes, decorative accents woven throughout as visual texture.
- **Domain fusion** — Every design fuses 2+ non-web domains (architecture, cinema, fashion, editorial, nature) making generic output structurally impossible.
- **Anti-slop ** — "Would 100 AIs converge on this? Yes → reject, different angle."

It also includes a completeness system that prevents the model from wrapping up early or leaving TODOs, and a planning phase (LOCK) that forces the model to commit to a specific aesthetic direction before writing any code.

## What's Inside

**Two variants of the same core skill**, tuned for different contexts:

### `Creative` (experimental)
For portfolios, landing pages, editorial sites, creative projects — anything where visual impact and originality matter most. Pushes hard on unconventional layouts, expressive typography, cinematic motion, and atmospheric depth.

This one is experimental. It produces genuinely surprising results but occasionally trades stability for ambition. If something breaks, dial it back or pair with simpler prompts.

### `SAAS`
Same design foundation, tuned for product interfaces — dashboards, SaaS landing pages, developer tools, fintech. Still distinctive and anti-slop, but channels creativity into structure, trust, and clarity rather than raw expression. Includes SF Mode for contexts where reliability and comprehension outrank visual spectacle.

## Quick Start

1. Drop the skill file into your project or paste into your AI's instructions
2. Ask for any frontend task
3. Say **"Prime"** for maximum quality

That's it. No setup, no dependencies, no config.

### Works With
- **Claude** / **ChatGPT** / **Gemini**
- **Cursor** / **Windsurf** / **Cline** / **Amplcode**
- Any tool that accepts skill files or system-level instructions

## Modes

### Prime Mode

Say **"Prime"** in your prompt. Works in both variants.

LLMs tend to take the path of least resistance — they'll pick the first decent font, the first reasonable color, the first layout that works, and call it done. Prime forces the model to slow down and actually design.

When active, the model analyzes every decision through multiple lenses (psychological, technical, accessibility, commercial, craft). It outputs up to 8 key design decisions with rationale before writing any code, committing to intentional choices instead of defaulting to whatever comes first. Every dimension gets cranked to peak.

### SF Mode (SaaS variant only)

Say **"SF"** in your prompt, or it activates automatically when the context is clearly SaaS, dashboards, devtools, fintech, or enterprise software.

This flips the creative philosophy: spectacle → trust, expression → clarity, surprise → comprehension. Think Stripe × Linear × Vercel energy. Typography favors readability over novelty. Color guides action, never dominates. Motion becomes pure feedback (150–350ms range), not choreography. Layouts use structured grids and calm hierarchy.

The creativity is still there — compressed into precision, alignment, and system consistency instead of visual experimentation. The verification gate shifts from "would someone screenshot this?" to "would Stripe ship this? Would a CTO trust this UI with company data?"


## Usage

```
Build a landing page for a climate data analytics startup.
```

With Prime:
```
Prime. Build a landing page for a climate data analytics startup.
```

With SF(Only available in SAAS skill):
```
SF. Build a dashboard for a real-time API monitoring tool.
```


## Skill Breakdown

| Section | What It Does |
|---------|-------------|
| **Anti-Slop** | Bans generic fonts, clichéd colors, cookie-cutter layouts. Anti-convergence across outputs. 100-AI litmus test. |
| **Completeness** | Every function defined, all states handled, no TODOs. Write to output limit. "Add later" is banned — add it now. |
| **Creative Direction** | Anchors every project in a conceptual metaphor with three DNA strands (surface, space, kinetics). Untraceable choices get cut. |
| **Typography** | Intentional font geometry, smart pairing across contrast axes, poster-grade hero composition with `clamp()` safety. |
| **Color** | 4-layer color system — L1 Neutral (spatial depth), L2 Accent (interactive ramp), L3 Semantic (perceptually matched), L4 Behavioral (responds to scroll/state). |
| **Composition** | Tension-based layouts, z-depth planes, creative structures, mathematical spacing. Every section a unique compositional problem. |
| **Motion** | Scan the interface for every motion opportunity, then implement. Custom easing per project, staggered choreography, physical weight on interactions. |
| **LOCK** | Mandatory pre-code planning: context, aesthetic direction, fusion domains, palette source, type strategy, signature motif, unforgettable moment, subversion. |
| **Prime** | Maximum quality trigger. Multi-lens analysis, innovation demands, stricter verification. |
| **SF** | SaaS specialization. Trust over spectacle, clarity over expression, disciplined precision. |

## Tips

- **Always use Prime** for important projects. It's the difference between "good" and "screenshot-worthy."
- **Give context.** "Landing page for a cybersecurity startup targeting enterprise CTOs" produces way better results than "landing page for a startup."
- **Let it plan.** The LOCK output isn't wasted tokens — it's what makes the design coherent.
- **Trust the fusion.** "Architecture + cinema" or "fashion + industrial" sounds weird but produces genuinely original designs.
- You can add your own rules in the Defaults section to customize behavior.

## Known Limitations

- Responsive design is significantly improved but not bulletproof — always check mobile output
- Very complex layouts occasionally have edge-case overflow issues
- The creative variant can be ambitious to the point of instability — that's by design, it's experimental
- Results vary across runs. This is partly intentional (the skill discourages convergence) and partly just how LLMs work
- Works best with models which are good at instructions following.


## How It Was Built
130+ iterations of the V1 skill using role reversal method. Then a month of V2 development including:

Real-world comparisons against Anthropic's official frontend skill.

Architectural reviews from multiple AI models

User testing and feedback from the community

Integrated some of the community's tricks,Claude frontend aesthetic and Leon lin's taste skill into this

## Contributing

Found a pattern that improves output? Open an issue or PR. Specifically useful:

- Failure cases with screenshots (what went wrong, which model, which variant)
- Instruction phrasings that improved adherence
- Before/after comparisons

---

*If these helped you build something cool, star the repo. It helps others find it. ⭐*







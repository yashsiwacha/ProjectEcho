'use client';

import { useEffect, useRef, useState } from 'react';
import * as THREE from 'three';

interface GraphNodeData {
  id: string;
  name: string;
  type: string;
  status: string;
  position: [number, number, number];
  color: number;
}

const GRAPH_NODES: GraphNodeData[] = [
  { id: '1', name: 'Career Passport', type: 'Identity Root', status: 'VERIFIED', position: [-2.2, 0.4, 0], color: 0xd4af37 },
  { id: '2', name: 'Evidence Proof', type: 'Cryptographic Claim', status: 'TIER 4', position: [-0.8, 1.2, 0.5], color: 0x10b981 },
  { id: '3', name: 'Taxonomy Match', type: 'Ontology Graph', status: 'VALIDATED', position: [-0.6, -1.0, -0.4], color: 0x06b6d4 },
  { id: '4', name: 'Rule Engine', type: 'Deterministic Evaluator', status: 'ELIGIBLE (100%)', position: [0.9, 0.5, 0.2], color: 0xd4af37 },
  { id: '5', name: 'Reasoning Card', type: 'Audit Trail', status: 'CERTIFIED', position: [2.2, -0.3, 0], color: 0x10b981 },
];

const EDGES = [
  [0, 1],
  [0, 2],
  [1, 3],
  [2, 3],
  [3, 4],
];

export default function ThreeDecisionGraph({
  onSelectNode,
  className = 'w-full h-[480px]',
}: {
  onSelectNode?: (node: GraphNodeData) => void;
  className?: string;
}) {
  const containerRef = useRef<HTMLDivElement>(null);
  const [selectedNode, setSelectedNode] = useState<GraphNodeData>(GRAPH_NODES[0]);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    const width = container.clientWidth || 800;
    const height = container.clientHeight || 480;

    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(45, width / height, 0.1, 1000);
    camera.position.set(0, 0, 5.2);

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(width, height);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    container.appendChild(renderer.domElement);

    const ambientLight = new THREE.AmbientLight(0xffffff, 0.9);
    scene.add(ambientLight);

    const dirLight = new THREE.DirectionalLight(0xd4af37, 2);
    dirLight.position.set(3, 4, 5);
    scene.add(dirLight);

    const group = new THREE.Group();
    scene.add(group);

    // 1. Create Node Meshes
    const nodeMeshes: THREE.Mesh[] = [];
    const sphereGeo = new THREE.SphereGeometry(0.22, 32, 32);

    GRAPH_NODES.forEach((node, i) => {
      const mat = new THREE.MeshStandardMaterial({
        color: node.color,
        emissive: node.color,
        emissiveIntensity: 0.5,
        roughness: 0.2,
        metalness: 0.8,
      });
      const mesh = new THREE.Mesh(sphereGeo, mat);
      mesh.position.set(...node.position);
      mesh.userData = { nodeIndex: i };
      group.add(mesh);
      nodeMeshes.push(mesh);

      // Glowing outer ring
      const ringGeo = new THREE.TorusGeometry(0.28, 0.015, 16, 48);
      const ringMat = new THREE.MeshBasicMaterial({ color: node.color, transparent: true, opacity: 0.6 });
      const ring = new THREE.Mesh(ringGeo, ringMat);
      ring.position.set(...node.position);
      group.add(ring);
    });

    // 2. Create Edges
    const lineMaterial = new THREE.LineBasicMaterial({ color: 0xd4af37, transparent: true, opacity: 0.35 });
    EDGES.forEach(([src, dst]) => {
      const p1 = new THREE.Vector3(...GRAPH_NODES[src].position);
      const p2 = new THREE.Vector3(...GRAPH_NODES[dst].position);
      const geom = new THREE.BufferGeometry().setFromPoints([p1, p2]);
      const line = new THREE.Line(geom, lineMaterial);
      group.add(line);
    });

    // 3. Traveling Energy Pulses across edges
    const pulseCount = EDGES.length;
    const pulseGeo = new THREE.SphereGeometry(0.04, 16, 16);
    const pulseMat = new THREE.MeshBasicMaterial({ color: 0xffffff });
    const pulseMeshes: THREE.Mesh[] = [];

    for (let i = 0; i < pulseCount; i++) {
      const pulse = new THREE.Mesh(pulseGeo, pulseMat);
      group.add(pulse);
      pulseMeshes.push(pulse);
    }

    // 4. Mouse Rotation & Raycaster
    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();
    let isDragging = false;
    let previousMouse = { x: 0, y: 0 };
    let rotX = 0;
    let rotY = 0;

    const onMouseDown = (e: MouseEvent) => {
      isDragging = true;
      previousMouse = { x: e.clientX, y: e.clientY };
    };

    const onMouseMove = (e: MouseEvent) => {
      const rect = container.getBoundingClientRect();
      mouse.x = ((e.clientX - rect.left) / width) * 2 - 1;
      mouse.y = -((e.clientY - rect.top) / height) * 2 + 1;

      if (isDragging) {
        const dx = e.clientX - previousMouse.x;
        const dy = e.clientY - previousMouse.y;
        rotY += dx * 0.005;
        rotX += dy * 0.005;
        previousMouse = { x: e.clientX, y: e.clientY };
      }
    };

    const onMouseUp = () => {
      isDragging = false;
      raycaster.setFromCamera(mouse, camera);
      const hits = raycaster.intersectObjects(nodeMeshes);
      if (hits.length > 0) {
        const idx = hits[0].object.userData.nodeIndex;
        const clicked = GRAPH_NODES[idx];
        setSelectedNode(clicked);
        if (onSelectNode) onSelectNode(clicked);
      }
    };

    container.addEventListener('mousedown', onMouseDown);
    window.addEventListener('mousemove', onMouseMove);
    window.addEventListener('mouseup', onMouseUp);

    // 5. Animation Loop
    let frameId: number;
    let clock = new THREE.Clock();

    const animate = () => {
      frameId = requestAnimationFrame(animate);
      const t = clock.getElapsedTime();

      group.rotation.y += (rotY - group.rotation.y) * 0.05 + 0.001;
      group.rotation.x += (rotX - group.rotation.x) * 0.05;

      // Animate pulses along edges
      EDGES.forEach(([src, dst], i) => {
        const p1 = new THREE.Vector3(...GRAPH_NODES[src].position);
        const p2 = new THREE.Vector3(...GRAPH_NODES[dst].position);
        const progress = (t * 0.8 + i * 0.3) % 1;
        const currentPos = new THREE.Vector3().lerpVectors(p1, p2, progress);
        pulseMeshes[i].position.copy(currentPos);
      });

      renderer.render(scene, camera);
    };

    animate();

    const handleResize = () => {
      if (!container) return;
      const w = container.clientWidth;
      const h = container.clientHeight;
      camera.aspect = w / h;
      camera.updateProjectionMatrix();
      renderer.setSize(w, h);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      cancelAnimationFrame(frameId);
      container.removeEventListener('mousedown', onMouseDown);
      window.removeEventListener('mousemove', onMouseMove);
      window.removeEventListener('mouseup', onMouseUp);
      window.removeEventListener('resize', handleResize);
      if (renderer.domElement && container.contains(renderer.domElement)) {
        container.removeChild(renderer.domElement);
      }
      renderer.dispose();
    };
  }, [onSelectNode]);

  return (
    <div className={`relative rounded-2xl overflow-hidden glass-panel-glow ${className}`}>
      <div ref={containerRef} className="w-full h-full cursor-grab active:cursor-grabbing" />

      {/* Floating Header */}
      <div className="absolute top-4 left-4 z-10 flex items-center gap-2 pointer-events-none">
        <span className="w-2.5 h-2.5 rounded-full bg-emerald-400 animate-ping" />
        <span className="text-xs font-mono font-semibold tracking-wider text-foreground uppercase bg-background/80 px-2.5 py-1 rounded-md border border-border backdrop-blur-md">
          3D Cryptographic DAG Trace
        </span>
      </div>

      {/* Selected Node Inspector Drawer */}
      {selectedNode && (
        <div className="absolute bottom-4 right-4 z-20 p-4 rounded-xl bg-card/95 border border-border shadow-2xl backdrop-blur-xl max-w-sm flex flex-col gap-2">
          <div className="flex items-center justify-between">
            <span className="text-[10px] font-mono text-accent font-bold uppercase tracking-wider">{selectedNode.type}</span>
            <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 font-bold border border-emerald-500/30">
              {selectedNode.status}
            </span>
          </div>
          <h3 className="font-bold text-base text-foreground">{selectedNode.name}</h3>
          <p className="text-xs text-muted-foreground">
            Deterministic node execution verified against immutable domain events.
          </p>
          <div className="pt-2 border-t border-border flex items-center justify-between text-xs font-mono text-muted-foreground">
            <span>Proof Hash: 0x7f8a...c9e1</span>
            <span>Execution Latency: 0.4ms</span>
          </div>
        </div>
      )}
    </div>
  );
}

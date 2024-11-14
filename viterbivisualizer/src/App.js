import React, { useState } from "react";
import TrellisGraph from "./TrellisGraph";
import "./App.css";

function App() {
  const [response, setResponse] = useState("");
  const [codedMessage, setCodedMessage] = useState("");
  const [channel, setChannel] = useState("");
  const [receivedMessage, setReceivedMessage] = useState("");
  const [decodedMessage, setDecodedMessage] = useState("");

  return (
    <div className="app">
      <GraphBackground response={response} />
      <DynamicButton
        setResponse={setResponse}
        codedMessage={codedMessage}
        setCodedMessage={setCodedMessage}
        channel={channel}
        setChannel={setChannel}
        receivedMessage={receivedMessage}
        setReceivedMessage={setReceivedMessage}
        decodedMessage={decodedMessage}
        setDecodedMessage={setDecodedMessage}
      />
    </div>
  );
}

function GraphBackground({ response }) {
  /*
  const nodes = [
    { time: 0, state: "00" },
    { time: 0, state: "01" },
    { time: 0, state: "10" },
    { time: 0, state: "11" },
    { time: 1, state: "00" },
    { time: 1, state: "01" },
    { time: 1, state: "10" },
    { time: 1, state: "11" },
    { time: 2, state: "00" },
    { time: 2, state: "01" },
    { time: 2, state: "10" },
    { time: 2, state: "11" },
    { time: 3, state: "00" },
    { time: 3, state: "01" },
    { time: 3, state: "10" },
    { time: 3, state: "11" },
    { time: 4, state: "00" },
    { time: 4, state: "01" },
    { time: 4, state: "10" },
    { time: 4, state: "11" },
    { time: 5, state: "00" },
    { time: 5, state: "01" },
    { time: 5, state: "10" },
    { time: 5, state: "11" },
  ];

  const links = [
    { source: { time: 0, state: "00" }, target: { time: 1, state: "00" } },
    { source: { time: 0, state: "00" }, target: { time: 1, state: "01" } },
    { source: { time: 0, state: "01" }, target: { time: 1, state: "10" } },
    { source: { time: 0, state: "01" }, target: { time: 1, state: "11" } },
    { source: { time: 1, state: "00" }, target: { time: 2, state: "00" } },
    { source: { time: 2, state: "00" }, target: { time: 3, state: "01" } },
  ];*/

  const nodes = [
    { time: 0, state: "000" },
    { time: 0, state: "001" },
    { time: 0, state: "010" },
    { time: 0, state: "011" },
    { time: 0, state: "100" },
    { time: 0, state: "110" },
    { time: 0, state: "111" },
    { time: 1, state: "000" },
    { time: 1, state: "001" },
    { time: 1, state: "010" },
    { time: 1, state: "011" },
    { time: 1, state: "100" },
    { time: 1, state: "110" },
    { time: 1, state: "111" },
    { time: 2, state: "000" },
    { time: 2, state: "001" },
    { time: 2, state: "010" },
    { time: 2, state: "011" },
    { time: 2, state: "100" },
    { time: 2, state: "110" },
    { time: 2, state: "111" },
  ];

  const links = [
    { source: { time: 0, state: "000" }, target: { time: 1, state: "000" } },
    { source: { time: 0, state: "000" }, target: { time: 1, state: "001" } },
    { source: { time: 0, state: "001" }, target: { time: 1, state: "010" } },
    { source: { time: 0, state: "001" }, target: { time: 1, state: "011" } },
  ];

  return (
    <div className="graph-container">
      {/* {response && <div className="response-message">{response}</div>} */}
      <TrellisGraph nodes={nodes} links={links} />
    </div>
  );
}

function DynamicButton({
  setResponse,
  codedMessage,
  setCodedMessage,
  channel,
  setChannel,
  receivedMessage,
  setReceivedMessage,
  decodedMessage,
  setDecodedMessage,
}) {
  const [showInputPage, setShowInputPage] = useState(false);
  const [closing, setClosing] = useState(false);
  const [requestFormData, setRequestFormData] = useState({
    message: "",
    polynomial: "",
    codedMessage: "",
    channel: "",
    recievedMessage: "",
    decodedMessage: "",
    codeWord: "",
  });

  const handleOpen = () => {
    setShowInputPage(true);
    setClosing(false);
  };

  const handleClose = () => {
    setClosing(true);
    setTimeout(() => {
      setShowInputPage(false);
      setClosing(false);
    }, 500);
  };

  //Input Field Handler
  const handleChange = (e) => {
    const { name, value } = e.target;
    setRequestFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  //Text Field Handler
  const handleTextChange = (e) => {
    const { name, value } = e.target;
    if (name === "codedMessage") {
      setCodedMessage(value);
    } else if (name === "channel") {
      setChannel(value);
    } else if (name === "receivedMessage") {
      setReceivedMessage(value);
    } else if (name === "decodedMessage") {
      setDecodedMessage(value);
    }

    setRequestFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleCode = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/code", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestFormData),
      });

      const result = await response.json();
      console.log("Response from backend:", result);
      setResponse(
        result.codedMessage + " " + result.channel || "No response message"
      );
      setCodedMessage(result.codedMessage || "No coded message");
      setChannel(result.channel || "No channel");
    } catch (error) {
      console.error("Error submitting form data:", error);
    }
  };

  const handleDecode = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/decode", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestFormData),
      });

      const result = await response.json();
      console.log("Response from backend:", result);
      setResponse(result.decodedMessage || "No response message");
      setReceivedMessage(result.receivedMessage || "No channel");
      setDecodedMessage(result.decodedMessage || "No decoded");
    } catch (error) {
      console.error("Error submitting form data:", error);
    }
  };

  return (
    <>
      <button className="dynamic-button" onClick={handleOpen}>
        Parametre
      </button>
      {showInputPage && (
        <div className={`input-page ${closing ? "slide-down" : "slide-up"}`}>
          <div className="input-container">
            <div className="input-group">
              <label>Správa:</label>
              <input
                type="text"
                name="message"
                placeholder="10011"
                value={requestFormData.message}
                onChange={handleChange}
              />
            </div>
            <div className="input-group">
              <label>Generujúci polynóm:</label>
              <input
                type="text"
                name="polynomial"
                placeholder="111;101;"
                value={requestFormData.polynomial}
                onChange={handleChange}
              />
            </div>
            <div className="input-group">
              <label>Kódovaná správa:</label>
              <textarea
                type="text"
                name="codedMessage"
                readOnly
                className="non-resizable-textarea"
                value={codedMessage}
                onChange={handleTextChange}
              />
            </div>
            <div className="input-group">
              <label>Chyby v prenose:</label>
              <input
                type="text"
                name="channel"
                value={channel}
                onChange={handleTextChange}
              />
            </div>
            <div className="input-group">
              <label>Prijatá správa:</label>
              <textarea
                type="text"
                name="receivedMessage"
                readOnly
                className="non-resizable-textarea"
                value={receivedMessage}
                onChange={handleTextChange}
              />
            </div>
            <div className="input-group">
              <label>Dekódovaná správa:</label>
              <input
                type="text"
                name="decodedMessage"
                readOnly
                className="non-resizable-textarea"
                value={decodedMessage}
                onChange={handleTextChange}
              />
            </div>
            <div className="input-group">
              <label>Kódové slovo:</label>
              <input
                type="text"
                name="codeWord"
                placeholder=""
                value={requestFormData.codeWord}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="button-container">
            <button onClick={handleCode}>Kóduj</button>
            <button onClick={handleDecode}>Dekóduj</button>
            <button onClick={handleClose}>Close</button>
          </div>
        </div>
      )}
    </>
  );
}
export default App;
